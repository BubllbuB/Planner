package com.example.planner.extensions

import android.database.Cursor
import com.example.planner.task.Task
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

fun Cursor.parseTask(): Task {
    val id = getInt(0)
    val title = getString(1)
    val description = getString(2)
    val fav = getInt(3) == 1
    val done = getInt(4) == 1

    return Task(title, description, id, fav, done)
}

fun String.getTaskMap(): SortedMap<Int, Task> {
    val itemType = object : TypeToken<SortedMap<Int, Task>>() {}.type
    return Gson().fromJson<SortedMap<Int, Task>>(this, itemType)
}

fun String.getTask(): Task {
    return Gson().fromJson<Task>(this, Task::class.java)
}