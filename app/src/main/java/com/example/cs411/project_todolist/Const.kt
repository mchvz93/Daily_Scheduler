package com.example.cs411.project_todolist

import android.os.FileObserver.CREATE

const val DB_NAME = "ToDoList"
const val DB_VERSION = 1
const val TABLE_FOLDER = "Folder"
const val COL_ID = "id"
const val COL_CREATED_AT = "createdAt"
const val COL_NAME = "name"


const val TABLE_TODO_TASK = "ToDoTask"
const val COL_TASK_ID = "taskId"
const val COL_TASK_NAME = "taskName"
const val COL_IS_COMPLETED = "isCompleted"



const val INTENT_TODO_ID = "TodoId"
const val INTENT_TODO_NAME = "TodoName"