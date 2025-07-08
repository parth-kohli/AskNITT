from database import get_connection
from datetime import datetime
def vote_answer(answer_id: int, user_id: int, vote: int):
    conn = get_connection()
    cursor = conn.cursor()
    cursor.execute("""
        INSERT INTO answer_votes (answer_id, user_id, vote, created_at)
        VALUES (%s, %s, %s, %s)
        ON DUPLICATE KEY UPDATE vote = %s, created_at = %s
    """, (answer_id, user_id, vote, datetime.utcnow(), vote, datetime.utcnow()))
    conn.commit()
    cursor.close()
    conn.close()
def remove_vote_answer(answer_id: int, user_id: int, vote: int):
    conn = get_connection()
    cursor = conn.cursor()
    cursor.execute("""
        DELETE FROM answer_votes WHERE answer_id = %s AND user_id = %s
    """, (answer_id, user_id))
    conn.commit()
    cursor.close()
    conn.close()