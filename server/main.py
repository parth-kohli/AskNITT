from fastapi import FastAPI
from routers import users, doubts, answers,friends
app = FastAPI()
app.include_router(users.router)
app.include_router(doubts.router)
app.include_router(answers.router)
app.include_router(friends.router)
