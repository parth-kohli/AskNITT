import mysql.connector
from mysql.connector import errorcode
from fastapi import HTTPException
import os

DB_NAME = "doubts_db"

TABLES = {
    "users": """
        CREATE TABLE IF NOT EXISTS users (
            id INT AUTO_INCREMENT PRIMARY KEY,
            username VARCHAR(64) UNIQUE NOT NULL,
            email VARCHAR(128),
            password VARCHAR(255),
            profile_pic LONGBLOB
        )
    """,
    "doubts": """
        CREATE TABLE IF NOT EXISTS doubts (
            id INT AUTO_INCREMENT PRIMARY KEY,
            title VARCHAR(255) NOT NULL,
            description TEXT,
            tags VARCHAR(255),
            is_anonymous TINYINT DEFAULT 1,
            user_id INT,
            created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
            FOREIGN KEY (user_id) REFERENCES users(id)
        )
    """,
    "answers": """
       CREATE TABLE IF NOT EXISTS answers (id INT PRIMARY KEY AUTO_INCREMENT,
    doubt_id INT NOT NULL,
    user_id INT NOT NULL,
    content TEXT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_anonymous TINYINT DEFAULT 1,
    FOREIGN KEY (doubt_id) REFERENCES doubts(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
)

    """,
    "attachments": """
        CREATE TABLE IF NOT EXISTS attachments (
            id INT AUTO_INCREMENT PRIMARY KEY,
            doubt_id INT,
            file_data LONGBLOB,
            file_type VARCHAR(100),
            file_name VARCHAR(255),
            FOREIGN KEY (doubt_id) REFERENCES doubts(id) ON DELETE CASCADE
        )
    """,
    "answer_attachments": """
       CREATE TABLE IF NOT EXISTS answer_attachments (
    id INT PRIMARY KEY AUTO_INCREMENT,
    answer_id INT NOT NULL,
    file_name VARCHAR(255),
    file_type VARCHAR(100),
    file_data LONGBLOB,
    FOREIGN KEY (answer_id) REFERENCES answers(id) ON DELETE CASCADE
)	
    """,
    "friend_requests": """
        CREATE TABLE IF NOT EXISTS friend_requests (
            id INT AUTO_INCREMENT PRIMARY KEY,
            sender_id INT NOT NULL,
            receiver_id INT NOT NULL,
            status ENUM('pending', 'accepted', 'rejected') DEFAULT 'pending',
            created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
            FOREIGN KEY (sender_id) REFERENCES users(id),
            FOREIGN KEY (receiver_id) REFERENCES users(id)
        )
    """,
    "answer_votes": """
        CREATE TABLE IF NOT EXISTS answer_votes (
            id INT PRIMARY KEY AUTO_INCREMENT,
    answer_id INT NOT NULL,
    user_id INT NOT NULL,
    vote TINYINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_vote (answer_id, user_id),
    FOREIGN KEY (answer_id) REFERENCES answers(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
        )
    """,
    "notifications": """
        CREATE TABLE IF NOT EXISTS notifications (
            id INT AUTO_INCREMENT PRIMARY KEY,
            user_id INT,
            message TEXT,
            is_read TINYINT DEFAULT 1,
            created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
            title VARCHAR(255),
            FOREIGN KEY (user_id) REFERENCES users(id)
        )
    """,
    "settings": """
        CREATE TABLE IF NOT EXISTS settings (
             id INT PRIMARY KEY,
            dark_mode BOOLEAN DEFAULT TRUE,
            notify_answers BOOLEAN DEFAULT TRUE,
            notify_friend_requests BOOLEAN DEFAULT TRUE,
            notify_updates BOOLEAN DEFAULT TRUE,
            notify_ai_answers BOOLEAN DEFAULT TRUE,
            FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE
        )
    """
}
def ensure_database_exists():
    try:
        conn = mysql.connector.connect(
            host="localhost",
            user="root",
            password="parthsarth9541"
        )
        cursor = conn.cursor()
        cursor.execute(f"CREATE DATABASE IF NOT EXISTS {DB_NAME}")
        conn.commit()
        cursor.close()
        conn.close()
    except mysql.connector.Error as e:
        raise HTTPException(status_code=500, detail=f"Error creating database: {str(e)}")
def ensure_tables_exist(conn):
    cursor = conn.cursor()
    for table_name, ddl in TABLES.items():
        try:
            cursor.execute(ddl)
        except mysql.connector.Error as err:
            raise HTTPException(status_code=500, detail=f"Error creating table {table_name}: {str(err)}")
    try:
       	     cursor.execute("SELECT * FROM users WHERE id=5")
       	     deepseek = cursor.fetchone()
             if not deepseek:
               image_path = "deepseek.png"
               if not os.path.exists(image_path):
                 raise HTTPException(status_code=500, detail="Profile picture not found")

               with open(image_path, "rb") as img_file:
                  profile_pic = img_file.read()
                

               cursor.execute("""
                INSERT INTO users (id, username, email, password, profile_pic)
                VALUES (%s, %s, %s, %s, %s)
            """, (5, "Deepseek", "deepseek@deepseek.com", "thisisdeepseekai", profile_pic))
             conn.commit()
    except mysql.connector.Error as err:
            raise HTTPException(status_code=500, detail=f"Error inserting Deepseek user: {str(err)}")

           
    cursor.close()

def get_connection():
    try:
        ensure_database_exists()
        conn = mysql.connector.connect(
            host="localhost",
            user="root",
            password="parthsarth9541",
            database=DB_NAME
        )
        ensure_tables_exist(conn)
        return conn
    except mysql.connector.Error as e:
        raise HTTPException(status_code=500, detail=str(e))
