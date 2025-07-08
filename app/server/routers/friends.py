from fastapi import APIRouter, Depends, Query, File, UploadFile, Form, Response
from schemas import DoubtCreateForm
from models import doubt
from auth import get_current_user, send_push_notification
from database import get_connection
from typing import Optional, List
from datetime import date
router = APIRouter()
@router.post("/friends/request")
def send_request(receiver_id: int, current_user: dict = Depends(get_current_user)):
    sender_id = current_user["id"]
    sender_name = current_user["username"]
    if sender_id == receiver_id:
        raise HTTPException(400, "Cannot friend yourself")
    conn = get_connection()
    cursor = conn.cursor()
    cursor.execute("""
        SELECT status FROM friend_requests
        WHERE sender_id = %s AND receiver_id = %s
    """, (sender_id, receiver_id))
    existing = cursor.fetchone()
    if existing:
        raise HTTPException(400, f"Friend request already {existing[0]}")
    cursor.execute("""
        INSERT INTO friend_requests (sender_id, receiver_id)
        VALUES (%s, %s)
    """, (sender_id, receiver_id))
    send_push_notification(receiver_id, f"{sender_name} sent you a friend request", "Accept or Reject the request")

    conn.commit()
    cursor.close()
    conn.close()
    return {"message": "Friend request sent"}
@router.post("/friends/delete")
def delete_friend(receiver_id: int, current_user: dict = Depends(get_current_user)):
    sender_id = current_user["id"]
    if sender_id == receiver_id:
        raise HTTPException(400, "Cannot unfriend yourself")
    conn = get_connection()
    cursor = conn.cursor()
    cursor.execute("""
        SELECT id FROM friend_requests
        WHERE (sender_id = %s AND receiver_id = %s)
           OR (sender_id = %s AND receiver_id = %s)
    """, (sender_id, receiver_id, receiver_id, sender_id))
    existing = cursor.fetchone()
    if not existing:
        raise HTTPException(404, "Friendship or request does not exist")
    cursor.execute("""
        DELETE FROM friend_requests
        WHERE (sender_id = %s AND receiver_id = %s)
           OR (sender_id = %s AND receiver_id = %s)
    """, (sender_id, receiver_id, receiver_id, sender_id))

    conn.commit()
    cursor.close()
    conn.close()
    return {"message": "Friendship or request removed"}

@router.post("/friends/respond")
def respond_request(request_id: int, action: str, current_user: dict = Depends(get_current_user)):
    if action not in ["accepted", "rejected"]:
        raise HTTPException(400, "Invalid action")
    conn = get_connection()
    cursor = conn.cursor()
    cursor.execute("""
        SELECT receiver_id FROM friend_requests
        WHERE id = %s
    """, (request_id,))
    request = cursor.fetchone()
    cursor.execute("""
        SELECT sender_id FROM friend_requests
        WHERE id = %s
    """, (request_id,))
    sender = cursor.fetchone()
    if not request or request[0] != current_user["id"]:
        raise HTTPException(403, "Not authorized")
    cursor.execute("""
        UPDATE friend_requests SET status = %s
        WHERE id = %s
    """, (action, request_id))
    send_push_notification(sender[0], f"{current_user['username']} {action} your friend request", "Your request was responded to")
    conn.commit()
    cursor.close()
    conn.close()
    return {"message": f"Friend request {action}"}

@router.get("/friends/pending")
def get_requests(current_user: dict = Depends(get_current_user)):
    conn = get_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute("""
        SELECT fr.id, u.username, u.id AS user_id, fr.created_at
        FROM friend_requests fr
        JOIN users u ON fr.sender_id = u.id
        WHERE fr.receiver_id = %s AND fr.status = 'pending'
    """, (current_user["id"],))

    requests = cursor.fetchall()
    cursor.close()
    conn.close()
    return requests
@router.get("/friends/sentpending")
def get_sentrequests(current_user: dict = Depends(get_current_user)):
    conn = get_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute("""
        SELECT fr.id, u.username, u.id AS user_id, fr.created_at
        FROM friend_requests fr
        JOIN users u ON fr.receiver_id = u.id
        WHERE fr.sender_id = %s AND fr.status = 'pending'
    """, (current_user["id"],))
    requests = cursor.fetchall()
    cursor.close()
    conn.close()
    return requests
@router.get("/friends/list")
def get_friends(current_user: dict = Depends(get_current_user)):
    user_id = current_user["id"]
    conn = get_connection()
    cursor = conn.cursor(dictionary=True)

    cursor.execute("""
        SELECT u.id, u.username
        FROM users u
        JOIN friend_requests fr ON (
            (fr.sender_id = u.id AND fr.receiver_id = %s)
            OR (fr.receiver_id = u.id AND fr.sender_id = %s)
        )
        WHERE fr.status = 'accepted'
    """, (user_id, user_id))

    friends = cursor.fetchall()
    cursor.close()
    conn.close()
    return friends
