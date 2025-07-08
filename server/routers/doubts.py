from fastapi import APIRouter, Depends, Query, File, UploadFile, Form, Response
from schemas import DoubtCreateForm
from models import doubt
from auth import get_current_user, send_push_notification
from database import get_connection
from typing import Optional, List
from datetime import date
router = APIRouter()

@router.post("/doubts/")
async def post_doubt(
    title: str = Form(...),
    description: Optional[str] = Form(None),
    tags: Optional[str] = Form(None),
    is_anonymous: bool = Form(False),
    files: Optional[List[UploadFile]] = File(default=None),
    current_user: dict = Depends(get_current_user)
):
    title = title.strip('"').strip("'")
    description = description.strip('"').strip("'") if description else None
    tags = tags.strip('"').strip("'") if tags else None
    user_id = current_user["id"]
    user_name = current_user["username"]
    conn = get_connection()
    cursor = conn.cursor()
    doubt_id = doubt.create_doubt(
        title=title,
        description=description,
        tags=tags,
        is_anonymous=is_anonymous,
	files=files,
        user_id=user_id
    )
    cursor.execute("SELECT sender_id from friend_requests where receiver_id = %s AND status = %s", (user_id,"accepted")) 
    friends=cursor.fetchall()
    cursor.execute("SELECT receiver_id from friend_requests where sender_id = %s AND status = %s", (user_id,"accepted"))
    friends+=cursor.fetchall()
    if friends and (not is_anonymous):
       for i in friends:
          send_push_notification(i[0], f"Your friend {user_name} asked a question", f"{title}")  
    return {"message": "Doubt posted", "doubt_id": doubt_id}
@router.get("/doubts/{doubt_id}/attachments")
def get_attachments(doubt_id: int):
    conn = get_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT id, file_name, file_type FROM attachments WHERE doubt_id = %s", (doubt_id,))
    return cursor.fetchall()
@router.get("/attachments/{attachment_id}")
def download_attachment(attachment_id: int):
    conn = get_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT file_data, file_type, file_name FROM attachments WHERE id = %s", (attachment_id,))
    result = cursor.fetchone()
    
    if result:
        return Response(
            content=result["file_data"],
            media_type=result["file_type"],
            headers={"Content-Disposition": f"inline; filename={result['file_name']}"}
        )
    raise HTTPException(status_code=404, detail="Attachment not found")
@router.get("/doubts")
def get_filtered_doubts(
    skip: int = Query(0, ge=0),
    limit: int = Query(10, ge=1),
    tag: Optional[str] = None,
    notuserid: Optional[int]=None,
    username: Optional[str] = None,
    start_date: Optional[date] = Query(None),
    end_date: Optional[date] = Query(None),
    current_user: dict = Depends(get_current_user)
):
    try:
        user_id = current_user["id"]
        conn = get_connection()
        cursor = conn.cursor(dictionary=True)

        query = """
        SELECT 
            d.id,
            d.title,
            d.description,
            d.tags,
            d.created_at,
            d.is_anonymous,
            u.username,
            u.id AS identity,
            CASE 
                WHEN fr.status = 'accepted' THEN 2 ELSE 0
            END
            - DATEDIFF(NOW(), d.created_at) AS priority_score
        FROM doubts d
        JOIN users u ON d.user_id = u.id
        LEFT JOIN friend_requests fr ON (
            fr.status = 'accepted' AND 
            (
                (fr.sender_id = %s AND fr.receiver_id = u.id)
                OR 
                (fr.receiver_id = %s AND fr.sender_id = u.id)
            )
        )
        """
        params = [user_id, user_id]
        filters = []

        if tag:
            filters.append("(d.tags LIKE %s OR d.description LIKE %s OR d.title LIKE %s)")
            params += [f"%{tag}%"] * 3
        if notuserid:
            filters.append("u.id != %s")
            params.append(notuserid)

        if username:
            filters.append("u.username = %s")
            params.append(username)

        if start_date:
            filters.append("DATE(d.created_at) >= %s")
            params.append(start_date)

        if end_date:
            filters.append("DATE(d.created_at) <= %s")
            params.append(end_date)

        if filters:
            query += " WHERE " + " AND ".join(filters)
        query += """
        ORDER BY priority_score DESC, d.created_at DESC
        LIMIT %s OFFSET %s
        """
        params += [limit, skip]
        cursor.execute(query, params)
        doubts = cursor.fetchall()
        cursor.close()
        conn.close()
        
        return doubts
    except Exception as e:
        print(e)
        return {"error": str(e)}
@router.get("/{username}/doubts")
def get_user_doubts(
    username: str,
):
    try:
        conn = get_connection()
        cursor = conn.cursor(dictionary=True)
        query = """
        SELECT d.id, d.title, d.description, d.tags, d.created_at, d.is_anonymous, u.username, u.id as identity
        FROM doubts d JOIN users u ON d.user_id = u.id
        """
        filters = []
        params = []
        if username:
            filters.append("u.username = %s")
            params.append(username)
        if filters:
            query += " WHERE " + " AND ".join(filters)

        query += " ORDER BY d.created_at DESC"
        cursor.execute(query, params)
        doubts = cursor.fetchall()
        cursor.close()
        conn.close()
        return doubts

    except Exception as e:
        return {"error": str(e)}
@router.delete("/doubts/{doubt_id}")
def delete_doubt(doubt_id: int, current_user: dict = Depends(get_current_user)):
    conn = get_connection()
    cursor = conn.cursor()

    # Check if the user owns the doubt
    cursor.execute("SELECT user_id FROM doubts WHERE id = %s", (doubt_id,))
    result = cursor.fetchone()
    if not result:
        raise HTTPException(status_code=404, detail="Doubt not found")
    if result[0] != current_user["id"]:
        raise HTTPException(status_code=403, detail="You are not authorized to delete this doubt")
    cursor.execute("DELETE FROM attachments WHERE doubt_id = %s", (doubt_id,))
    cursor.execute("DELETE FROM doubts WHERE id = %s", (doubt_id,))
    conn.commit()
    cursor.close()
    conn.close()
    return {"message": "Doubt deleted successfully"}
@router.put("/doubts/{doubt_id}")
def update_doubt(
    doubt_id: int,
    title: str = Form(...),
    description: str = Form(...),
    tags: str = Form(...),
    is_anonymous: int = Form(...),
    attachments_to_delete: Optional[str] = Form(None), 
    new_files: Optional[List[UploadFile]] = File(None),
    current_user: dict = Depends(get_current_user)
):
    user_id = current_user["id"]
    conn = get_connection()
    cursor = conn.cursor()

    cursor.execute("SELECT user_id FROM doubts WHERE id = %s", (doubt_id,))
    owner = cursor.fetchone()
    if not owner or owner[0] != user_id:
        return {"error": "Not authorized"}
    cursor.execute("""
        UPDATE doubts
        SET title = %s, description = %s, tags = %s, is_anonymous = %s
        WHERE id = %s
    """, (title, description, tags, is_anonymous, doubt_id))
    if attachments_to_delete:
        ids = [int(i) for i in attachments_to_delete.split(",") if i.strip().isdigit()]
        if ids:
            cursor.execute(
                f"DELETE FROM attachments WHERE id IN ({','.join(['%s']*len(ids))}) AND doubt_id = %s",
                (*ids, doubt_id)
            )
    if new_files:
        for file in new_files:
            file_data = file.file.read()
            cursor.execute("""
                INSERT INTO attachments (doubt_id, file_data, file_type, file_name)
                VALUES (%s, %s, %s, %s)
            """, (doubt_id, file_data, file.content_type, file.filename))

    conn.commit()
    cursor.close()
    conn.close()
    return {"message": "Doubt updated"}