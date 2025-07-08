from jose import jwt, JWTError
from datetime import datetime, timedelta
from passlib.context import CryptContext
from fastapi import Depends, HTTPException, status
from fastapi.security import OAuth2PasswordBearer
from database import get_connection
SECRET_KEY = "supersneakysecretkey"
ALGORITHM = "HS256"
ACCESS_TOKEN_EXPIRE_MINUTES = 30
pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")
oauth2_scheme = OAuth2PasswordBearer(tokenUrl="login")
def send_push_notification(user_id: int, title: str, body: str):
    conn = get_connection()
    cursor = conn.cursor()
    cursor.execute("INSERT INTO notifications(user_id, title, message, is_read, created_at) VALUES (%s, %s, %s, %s, %s) ", (user_id,title, body, 0, datetime.utcnow()))
    conn.commit()
    cursor.close()
    conn.close()
    return {"message_id": "done"}
def hash_password(password: str):
    return pwd_context.hash(password)
def verify_password(plain: str, hashed: str):
    return pwd_context.verify(plain, hashed)
def create_token(data: dict):
    to_encode = data.copy()
    expire = datetime.utcnow() + timedelta(days=ACCESS_TOKEN_EXPIRE_MINUTES)
    to_encode.update({"exp": expire})
    return jwt.encode(to_encode, SECRET_KEY, algorithm=ALGORITHM)
def decode_token(token: str):
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        return payload.get("sub")
    except JWTError:
        raise HTTPException(status_code=401, detail="Invalid token")
def get_current_user(token: str = Depends(oauth2_scheme)):
    email = decode_token(token)
    conn = get_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT * FROM users WHERE email=%s", (email,))
    user = cursor.fetchone()
    cursor.close()
    conn.close()
    if not user:
        raise HTTPException(status_code=401, detail="User not found")
    return user