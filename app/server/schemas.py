from pydantic import BaseModel
from typing import Optional
from typing import List
from fastapi import UploadFile, File, Form, Depends
from fastapi import APIRouter, Depends, Form, File, UploadFile
from typing import List, Optional

class UserCreate(BaseModel):
    username: str
    email: str
    password: str

class UserLogin(BaseModel):
    email: str
    password: str



class DoubtCreateForm(BaseModel):
    title: str
    description: Optional[str] = None
    tags: Optional[str] = None
    is_anonymous: Optional[bool] = False
class AnswerCreateForm(BaseModel):
    doubtid: int
    content: str
    is_anonymous: Optional[bool] = False
