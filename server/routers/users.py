from fastapi import APIRouter, HTTPException, Depends, Response, Query, Form, Body
from pydantic import BaseModel
from fastapi.security import OAuth2PasswordRequestForm
from fastapi import File, UploadFile
from auth import get_current_user, hash_password, verify_password
from database import get_connection
from models import user
from schemas import UserCreate, UserLogin
import imghdr
from typing import Optional
from auth import hash_password, verify_password, create_token


router = APIRouter()
@router.post("/uploadpfp/")
def upload_blob(file: UploadFile = File(...), current_user: dict = Depends(get_current_user)):
    conn = get_connection()
    cursor = conn.cursor(dictionary=True)

    image_bytes = file.file.read()
    user_id = current_user["id"]

    cursor.execute("UPDATE users SET profile_pic = %s WHERE id = %s", (image_bytes, user_id))
    conn.commit()
    return {"message": "Blob uploaded"}

@router.post("/signup")
def signup(data: UserCreate):
    if user.get_user_by_email(data.email):
        raise HTTPException(status_code=400, detail="Email already exists")
    user_id = user.create_user(data.username, data.email, hash_password(data.password))
    return {"message": "User created", "user_id": user_id}

@router.post("/login")
def login(form_data: OAuth2PasswordRequestForm = Depends()):
    u = user.get_user_by_email(form_data.username)  # username is email in this case
    if not u or not verify_password(form_data.password, u["password"]):
        raise HTTPException(status_code=401, detail="Invalid credentials")
    token = create_token({"sub": u["email"]})
    return {"access_token": token, "token_type": "bearer", "user_id": u["id"], "username": u["username"] }
@router.post("/change-password")
def change_password(
    old_password: str = Form(...),
    new_password: str = Form(...),
    current_user: dict = Depends(get_current_user)
):
    user_id = current_user["id"]
    conn = get_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT password FROM users WHERE id = %s", (user_id,))
    user = cursor.fetchone()
    if not user or not verify_password(old_password, user["password"]):
        raise HTTPException(status_code=403, detail="Incorrect current password")
    new_hashed = hash_password(new_password)
    cursor.execute("UPDATE users SET password = %s WHERE id = %s", (new_hashed, user_id))
    conn.commit()
    cursor.close()
    conn.close()
    return {"message": "Password changed successfully"}
@router.post("/edit-profile")
async def edit_profile(
    username: Optional[str] = Form(None),
    email: Optional[str] = Form(None),
    pfp: Optional[UploadFile] = File(None),
    current_user: dict = Depends(get_current_user)
):
    user_id = current_user["id"]
    conn = get_connection()
    cursor = conn.cursor()
    if username:
        cursor.execute("UPDATE users SET username = %s WHERE id = %s", (username, user_id))
    if email:
        cursor.execute("UPDATE users SET email = %s WHERE id = %s", (email, user_id))
    if pfp:
        pfp_data = pfp.file.read()
        cursor.execute(
            "UPDATE users SET profile_pic = %s WHERE id = %s", (pfp_data, user_id)
        )
    conn.commit()
    cursor.close()
    conn.close()
    return {"message": "Profile updated successfully"}
@router.get("/settings")
def get_user_settings(current_user: dict = Depends(get_current_user)):
    conn = get_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT * FROM settings WHERE id = %s", (current_user["id"],))
    settings = cursor.fetchone()
    cursor.close()
    conn.close()
    if not settings:
        raise HTTPException(404, "Settings not found") 
    print(settings)
    settings.pop("id")
    for i in settings:
       settings[i]=settings[i]==1
    print(settings)
    return settings
class SettingsUpdate(BaseModel):
    dark_mode: bool
    notify_answers: bool
    notify_friend_requests: bool
    notify_updates: bool
    notify_ai_answers: bool
@router.post("/settings/update")
def update_settings(
    settings: SettingsUpdate,
    current_user: dict = Depends(get_current_user)
):
    conn = get_connection()
    cursor = conn.cursor()
    cursor.execute("""
        INSERT INTO settings (id, dark_mode, notify_answers, notify_friend_requests, notify_updates, notify_ai_answers)
        VALUES (%s, %s, %s, %s, %s, %s)
        ON DUPLICATE KEY UPDATE
            dark_mode = VALUES(dark_mode),
            notify_answers = VALUES(notify_answers),
            notify_friend_requests = VALUES(notify_friend_requests),
            notify_updates = VALUES(notify_updates),
            notify_ai_answers = VALUES(notify_ai_answers)
    """, (current_user["id"], settings.dark_mode, settings.notify_answers, settings.notify_friend_requests, settings.notify_updates, settings.notify_ai_answers))
    conn.commit()
    cursor.close()
    conn.close()
    return {"message": "Settings updated successfully"}


@router.post("/loginwithtoken")
def loginwithtoken(current_user: dict = Depends(get_current_user)):
    user_id = current_user["id"]
    return {"id": current_user["id"], "username": current_user["username"] }
@router.get("/checknotif")
def checknotif(current_user: dict = Depends(get_current_user)):
    user_id = current_user["id"]
    conn = get_connection()
    cursor = conn.cursor()
    cursor.execute("SELECT * FROM notifications where user_id = %s and is_read = %s", (current_user["id"], 0))
    notifications = cursor.fetchone()
    if notifications:
    	return {"id":notifications[0], "user_id":notifications[1], "message":notifications[2], "is_read":notifications[3], "created_at":notifications[4], "title":notifications[5]}
    else: 
        return None
@router.post("/notifications/{notif_id}/read")
def checknotif(notif_id: int,current_user: dict = Depends(get_current_user)):
    user_id = current_user["id"]
    conn = get_connection()
    cursor = conn.cursor()
    cursor.execute("SELECT * FROM notifications where id = %s", (notif_id,))
    notifications = cursor.fetchone()
    
    if not notifications:
       return {"message": "Notification doesn't exist"}
    cursor.execute("DELETE FROM notifications where id = %s", (notif_id,))
    conn.commit()
    cursor.close()
    conn.close()
    return notifications

@router.get("/users/{user_id}/pfp")
def get_pfp(user_id: int):
    conn = get_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT profile_pic FROM users WHERE id = %s", (user_id,))
    result = cursor.fetchone()

    image_type = imghdr.what(None, h=result["profile_pic"])
    media_type = f"image/{image_type}" if image_type else "application/octet-stream"
    print(media_type)
    if result["profile_pic"] is None:
        return FileResponse("src/main/res/drawable/defaultpfp.jpg")
    return Response(content=result["profile_pic"], media_type=media_type)
@router.get("/users")
def get_users(skip: int = Query(0, ge=0), limit: int = Query(10, ge=1), tag: Optional[str] = None):
    conn = get_connection()
    cursor = conn.cursor(dictionary=True)

    filters = []
    params = []

    query = """
        SELECT u.id, u.username,
               (SELECT COUNT(*) FROM doubts WHERE user_id = u.id) AS questions,
               (SELECT COUNT(*) FROM answers WHERE user_id = u.id) AS answers
        FROM users u
    """

    if tag:
        filters.append("u.username LIKE %s")
        params.append(f"%{tag}%")

    if filters:
        query += " WHERE " + " AND ".join(filters)

    query += " GROUP BY u.id  ORDER BY questions + answers DESC LIMIT %s OFFSET %s"
    params.extend([limit, skip])

    cursor.execute(query, params)
    users = cursor.fetchall()
    cursor.close()
    conn.close()
    return users


