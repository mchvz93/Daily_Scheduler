package com.example.cs411.project_todolist.DTO

class Folder {

    var id: Long = -1
    var name = ""
    var createdAt = ""
    var items: MutableList<ToDoTask> = ArrayList()

}