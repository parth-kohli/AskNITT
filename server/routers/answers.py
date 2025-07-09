
from fastapi import APIRouter, Depends, Form, File, UploadFile
import fitz  
import docx
from database import get_connection
from datetime import datetime
from auth import get_current_user, send_push_notification
from models import answer
from typing import List, Optional
from schemas import AnswerCreateForm
from openai import OpenAI
from fastapi import HTTPException
from base64 import b64encode
router = APIRouter()
def extract_text_from_pdf_bytes(data: bytes) -> str:
    doc = fitz.open(stream=data, filetype="pdf")
    
    return "\n".join([page.get_text() for page in doc])

def extract_text_from_docx_bytes(data: bytes) -> str:
    with tempfile.NamedTemporaryFile(delete=False, suffix=".docx") as tmp:
        tmp.write(data)
        tmp.seek(0)
        doc = docx.Document(tmp.name)
        return "\n".join([para.text for para in doc.paragraphs])

def encode_image_bytes_to_base64(data: bytes, file_type: str) -> dict:
    base64_image = b64encode(data).decode("utf-8")
    print(f"data:{file_type};base64,{base64_image}")
    return {
        "type": "image_url",
        "image_url": {
            "url": f"data:{file_type};base64,{base64_image}",
            "detail": "auto"
        }
    }

@router.post("/answers/")
async def post_answer(
    doubt_id: int = Form(...),
    answer_text: str = Form(...),
    is_anonymous: int = Form(...),
    files: List[UploadFile] = File([]),
    current_user: dict = Depends(get_current_user)
):
    conn = get_connection()
    cursor = conn.cursor(dictionary=True)
    user_id = current_user["id"]
    user_name = current_user["username"]
    answer_text=answer_text.strip('"').strip("'")
    cursor.execute("INSERT INTO answers (doubt_id, user_id, content, is_anonymous) VALUES (%s, %s, %s, %s)", (doubt_id, user_id, answer_text, is_anonymous))
    answer_id = cursor.lastrowid
    for file in files:
        content = await file.read()
        cursor.execute("INSERT INTO answer_attachments (answer_id, file_data, file_type, file_name) VALUES (%s, %s, %s, %s)",
                       (answer_id, content, file.content_type, file.filename))
    cursor.execute("SELECT user_id, title FROM doubts WHERE id=%s", (doubt_id,))
    qsuser = cursor.fetchone()
    conn.commit()
    cursor.close()
    conn.close()
    send_push_notification(qsuser["user_id"], f"{user_name} answered your question", f"{qsuser['title']}") 
    return {"message": "Answer posted successfully", "answer_id": answer_id}
@router.put("/answers/{answer_id}")
def update_doubt(
    answer_id: int,
    answer_text: str = Form(...),
    is_anonymous: int = Form(...),
    attachments_to_delete: Optional[str] = Form(None), 
    new_files: Optional[List[UploadFile]] = File(None),
    current_user: dict = Depends(get_current_user)
):
    user_id = current_user["id"]
    conn = get_connection()
    cursor = conn.cursor()

    cursor.execute("SELECT user_id FROM answers WHERE id = %s", (answer_id,))
    owner = cursor.fetchone()
    if not owner or owner[0] != user_id:
        return {"error": "Not authorized"}
    cursor.execute("""
        UPDATE answers
        SET content = %s, is_anonymous = %s
        WHERE id = %s
    """, (answer_text, is_anonymous, answer_id))
    if attachments_to_delete:
        ids = [int(i) for i in attachments_to_delete.split(",") if i.strip().isdigit()]
        if ids:
            cursor.execute(
                f"DELETE FROM answer_attachments WHERE id IN ({','.join(['%s']*len(ids))}) AND answer_id = %s",
                (*ids, answer_id)
            )
    if new_files:
        for file in new_files:
            file_data = file.file.read()
            cursor.execute("""
                INSERT INTO answer_attachments (answer_id, file_data, file_type, file_name)
                VALUES (%s, %s, %s, %s)
            """, (answer_id, file_data, file.content_type, file.filename))

    conn.commit()
    cursor.close()
    conn.close()
    return {"message": "Answer updated"}

@router.get("/doubts/{doubt_id}/answers")
def get_answers(doubt_id: int, current_user: dict = Depends(get_current_user)):
    user_id = current_user["id"]
    conn = get_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute("""
        SELECT 
            a.id,
            a.content,
            a.created_at,
	    a.is_anonymous,
            u.username,
            u.id as user_id,
	    COALESCE(SUM(CASE WHEN av.vote = 1 THEN 1 ELSE 0 END), 0) AS upvotes,
            COALESCE(SUM(CASE WHEN av.vote = -1 THEN 1 ELSE 0 END), 0) AS downvotes,
            MAX(CASE WHEN av2.user_id = %s THEN av2.vote ELSE 0 END) AS user_vote
        FROM answers a
        JOIN users u ON a.user_id = u.id
        LEFT JOIN answer_votes av ON av.answer_id = a.id
        LEFT JOIN answer_votes av2 ON av2.answer_id = a.id AND av2.user_id = %s
        WHERE a.doubt_id = %s 
        GROUP BY a.id
        ORDER BY 
    		CASE WHEN u.id = 5 THEN 0 ELSE 1 END, 
   		(upvotes - downvotes) DESC   
    """, (user_id, user_id, doubt_id))
    answers = cursor.fetchall()
    cursor.close()
    conn.close()
    return answers
@router.get("/users/{username}/answers")
def get_answers(username: str, current_user: dict = Depends(get_current_user)):
    user_id = current_user["id"]
    conn = get_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute("""
        SELECT 
            a.id,
            a.content,
            a.created_at,
	    a.is_anonymous,
            u.username,
            u.id as user_id,
	    COALESCE(SUM(CASE WHEN av.vote = 1 THEN 1 ELSE 0 END), 0) AS upvotes,
            COALESCE(SUM(CASE WHEN av.vote = -1 THEN 1 ELSE 0 END), 0) AS downvotes,
            MAX(CASE WHEN av2.user_id = %s THEN av2.vote ELSE 0 END) AS user_vote
        FROM answers a
        JOIN users u ON a.user_id = u.id
        LEFT JOIN answer_votes av ON av.answer_id = a.id
        LEFT JOIN answer_votes av2 ON av2.answer_id = a.id AND av2.user_id = %s
        WHERE u.username = %s
        GROUP BY a.id
        ORDER BY (a.created_at) DESC
    """, (user_id, user_id, username))
    answers = cursor.fetchall()
    cursor.close()
    conn.close()
    return answers



@router.post("/answers/{answer_id}/vote")
def vote_on_answer(
    answer_id: int,
    vote: int = Form(...),  
    current_user: dict = Depends(get_current_user)
):
    if vote not in (1, -1):
       if vote==0:
          user_id = current_user["id"]
          answer.remove_vote_answer(answer_id, user_id, vote)
          return {"message": "Vote deleted"}
       else:
          raise HTTPException(status_code=400, detail="Vote must be 1 (upvote) or -1 (downvote)")
    
    user_id = current_user["id"]
    answer.vote_answer(answer_id, user_id, vote)
    return {"message": "Vote recorded"}
@router.get("/answers/{answer_id}/attachments")
def get_attachments(answer_id: int):
    conn = get_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT id, file_name, file_type FROM answer_attachments WHERE answer_id = %s", (answer_id,))
    return cursor.fetchall()
@router.get("/answerattachments/{attachment_id}")
def download_attachment(attachment_id: int):
    conn = get_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT file_data, file_type, file_name FROM answer_attachments WHERE id = %s", (attachment_id,))
    result = cursor.fetchone()
    if result:
        return Response(
            content=result["file_data"],
            media_type=result["file_type"],
            headers={"Content-Disposition": f"inline; filename={result['file_name']}"}
        )
    raise HTTPException(status_code=404, detail="Attachment not found")
@router.delete("/answers/{answer_id}")
def delete_doubt(answer_id: int, current_user: dict = Depends(get_current_user)):
    conn = get_connection()
    cursor = conn.cursor()
    cursor.execute("SELECT user_id FROM answers WHERE id = %s", (answer_id,))
    result = cursor.fetchone()
    if not result:
        raise HTTPException(status_code=404, detail="Answer not found")
    if result[0] != current_user["id"]:
        raise HTTPException(status_code=403, detail="You are not authorized to delete this Answer")
    cursor.execute("DELETE FROM answer_attachments WHERE answer_id = %s", (answer_id,))
    cursor.execute("DELETE FROM answers WHERE id = %s", (answer_id,))
    conn.commit()
    cursor.close()
    conn.close()
    return {"message": "Answer deleted successfully"}
def get_attachments_from_db(doubt_id: int):
    conn = get_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT file_data, file_type, file_name FROM attachments WHERE doubt_id = %s", (doubt_id,))
    files = cursor.fetchall()
    cursor.close()
    conn.close()
    return files
@router.post("/doubts/{doubt_id}/askai")
async def ask_ai(
    doubt_id: int,
    title: str = Form(...),
    description: str = Form(...),
    tags: str = Form(...),
    current_user: dict = Depends(get_current_user)
):
    client = OpenAI(
        base_url="https://openrouter.ai/api/v1",
        api_key="api-key", 
    )

    content_parts = [{
        "type": "text",
        "text": f"Title: {title}\n\nDescription: {description}\n\nTags: {tags}"
    }]

    skipped_files_note = ""

    attachments = get_attachments_from_db(doubt_id)
    for file in attachments:
        data = file["file_data"]
        mime = file["file_type"]

       
        if mime == "application/pdf":
            try:
                text = extract_text_from_pdf_bytes(data)
                content_parts.append({"type": "text", "text": f"[Extracted from PDF]:\n{text}"})
            except:
                skipped_files_note += f"Failed to read PDF: {file['file_name']}\n"
        elif mime in ["application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"]:
            try:
                text = extract_text_from_docx_bytes(data)
                content_parts.append({"type": "text", "text": f"[Extracted from DOCX]:\n{text}"})
            except:
                skipped_files_note += f"Failed to read DOCX: {file['file_name']}\n"
        else:
            skipped_files_note += f"Skipped unsupported file: {file['file_name']}\n"
    print(content_parts)

    completion = client.chat.completions.create(
        model="deepseek/deepseek-r1-0528-qwen3-8b:free",
        messages=[{
            "role": "user",
            "content": content_parts
        }]
    )

    ai_response = skipped_files_note + completion.choices[0].message.content

    conn = get_connection()
    cursor = conn.cursor()
    cursor.execute("""
        INSERT INTO answers (content, user_id, doubt_id, is_anonymous, created_at)
        VALUES (%s, %s, %s, %s, %s)
    """, (ai_response, 5, doubt_id, 0, datetime.utcnow()))
    send_push_notification(current_user["id"], f"DEEPSEEK answered your question using ai", f"{title}") 
    conn.commit()
    cursor.close()
    conn.close()

    return {"message": "AI Answer saved"}
