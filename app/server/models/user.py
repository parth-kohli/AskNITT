from database import get_connection

def create_user(username, email, password_hash):
    conn = get_connection()
    cursor = conn.cursor()
    cursor.execute("INSERT INTO users (username, email, password) VALUES (%s, %s, %s)",
                   (username, email, password_hash))
    conn.commit()
    user_id = cursor.lastrowid
    cursor.execute("INSERT INTO settings (id) VALUES (%s)",
                   (user_id,))
    conn.commit()
    cursor.close()
    conn.close()
    return user_id

def get_user_by_email(email):
    conn = get_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT * FROM users WHERE email=%s", (email,))
    user = cursor.fetchone()
    cursor.close()
    conn.close()
    return user
