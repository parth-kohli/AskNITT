from database import get_connection
from datetime import datetime
from typing import List
from fastapi import UploadFile, File, Form, Depends

def create_doubt(title, description, tags, is_anonymous, files, user_id):
    conn = get_connection()
    cursor = conn.cursor()
    cursor.execute("""
        INSERT INTO doubts (title, description, tags, is_anonymous, user_id, created_at)
        VALUES (%s, %s, %s, %s, %s, %s)
    """, (title, description, tags, is_anonymous, user_id, datetime.utcnow()))
    doubt_id = cursor.lastrowid
    if files:
      for file in files:
        file_data = file.file.read()
        file_type = file.content_type
        file_name = file.filename

        cursor.execute(
            "INSERT INTO attachments (doubt_id, file_data, file_type, file_name) VALUES (%s, %s, %s, %s)",
            (doubt_id, file_data, file_type, file_name)
        )


    conn.commit()
    cursor.close()
    conn.close()
