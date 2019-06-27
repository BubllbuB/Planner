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

fun String.getTaskMap(): SortedMap<Int, Task> =
    Gson().fromJson(this, object : TypeToken<SortedMap<Int, Task>>() {}.type)


fun String.getTask(): Task = Gson().fromJson(this, Task::class.java)

fun getOffset(posHeadOther: Int, position: Int, firstFav: Boolean): Int {
    return if (posHeadOther in 1..position && firstFav) {
        2
    } else if (posHeadOther > 0 && firstFav) {
        1
    } else {
        0
    }
}