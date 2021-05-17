package com.example.cs411.project_todolist

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.cs411.project_todolist.DTO.Folder
import com.example.cs411.project_todolist.DTO.ToDoTask

class DBHandler(val context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val createToDoTable = "  CREATE TABLE $TABLE_FOLDER (" +
                "$COL_ID integer PRIMARY KEY AUTOINCREMENT," +
                "$COL_CREATED_AT datetime DEFAULT CURRENT_TIMESTAMP," +
                "$COL_NAME varchar);"
        val createToDoItemTable =
            "CREATE TABLE $TABLE_TODO_TASK (" +
                    "$COL_ID integer PRIMARY KEY AUTOINCREMENT," +
                    "$COL_CREATED_AT datetime DEFAULT CURRENT_TIMESTAMP," +
                    "$COL_TASK_ID integer," +
                    "$COL_TASK_NAME varchar," +
                    "$COL_IS_COMPLETED integer);"

        db.execSQL(createToDoTable)
        db.execSQL(createToDoItemTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }


    fun addToDo(toDo: Folder): Boolean {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(COL_NAME, toDo.name)
        val result = db.insert(TABLE_FOLDER, null, cv)
        return result != (-1).toLong()
    }

    fun updateToDo(toDo: Folder) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(COL_NAME, toDo.name)
        db.update(
            TABLE_FOLDER,cv,"$COL_ID=?" , arrayOf(toDo.id
            .toString()))
    }

    fun deleteToDo(todoId: Long){
        val db = writableDatabase
        db.delete(TABLE_TODO_TASK,"$COL_TASK_ID=?", arrayOf(todoId.toString()))
        db.delete(TABLE_FOLDER,"$COL_ID=?", arrayOf(todoId.toString()))
    }

    fun updateToDoItemCompletedStatus(todoId: Long,isCompleted: Boolean){
        val db = writableDatabase
        val queryResult = db.rawQuery("SELECT * FROM $TABLE_TODO_TASK WHERE $COL_TASK_ID=$todoId", null)

        if (queryResult.moveToFirst()) {
            do {
                val item = ToDoTask()
                item.id = queryResult.getLong(queryResult.getColumnIndex(COL_ID))
                item.taskId = queryResult.getLong(queryResult.getColumnIndex(COL_TASK_ID))
                item.taskName = queryResult.getString(queryResult.getColumnIndex(COL_TASK_NAME))
                item.isCompleted = isCompleted
                updateToDoItem(item)
            } while (queryResult.moveToNext())
        }

        queryResult.close()
    }

    fun getToDos(): MutableList<Folder> {
        val result: MutableList<Folder> = ArrayList()
        val db = readableDatabase
        val queryResult = db.rawQuery("SELECT * from $TABLE_FOLDER", null)
        if (queryResult.moveToFirst()) {
            do {
                val todo = Folder()
                todo.id = queryResult.getLong(queryResult.getColumnIndex(COL_ID))
                todo.name = queryResult.getString(queryResult.getColumnIndex(COL_NAME))
                result.add(todo)
            } while (queryResult.moveToNext())
        }
        queryResult.close()
        return result
    }

    fun addToDoItem(item: ToDoTask): Boolean {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(COL_TASK_NAME, item.taskName)
        cv.put(COL_TASK_ID, item.taskId)
        cv.put(COL_IS_COMPLETED, item.isCompleted)

        val result = db.insert(TABLE_TODO_TASK, null, cv)
        return result != (-1).toLong()
    }

    fun updateToDoItem(item: ToDoTask) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(COL_TASK_NAME, item.taskName)
        cv.put(COL_TASK_ID, item.taskId)
        cv.put(COL_IS_COMPLETED, item.isCompleted)

        db.update(TABLE_TODO_TASK, cv, "$COL_ID=?", arrayOf(item.id.toString()))
    }

    fun deleteToDoItem(itemId : Long){
        val db = writableDatabase
        db.delete(TABLE_TODO_TASK,"$COL_ID=?" , arrayOf(itemId.toString()))
    }

    fun getToDoItems(todoId: Long): MutableList<ToDoTask> {
        val result: MutableList<ToDoTask> = ArrayList()

        val db = readableDatabase
        val queryResult = db.rawQuery("SELECT * FROM $TABLE_TODO_TASK WHERE $COL_TASK_ID=$todoId", null)

        if (queryResult.moveToFirst()) {
            do {
                val item = ToDoTask()
                item.id = queryResult.getLong(queryResult.getColumnIndex(COL_ID))
                item.taskId = queryResult.getLong(queryResult.getColumnIndex(COL_TASK_ID))
                item.taskName = queryResult.getString(queryResult.getColumnIndex(COL_TASK_NAME))
                item.isCompleted = queryResult.getInt(queryResult.getColumnIndex(COL_IS_COMPLETED)) == 1
                result.add(item)
            } while (queryResult.moveToNext())
        }
        queryResult.close()
        return result
    }

}