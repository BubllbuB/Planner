package com.example.planner.storages

import com.example.planner.task.Task
import java.util.*

interface Storage {
    fun addTask(task: Task?)

    fun removeTask(task: Task?)

    fun editTask(task: Task?)

    fun getList(): SortedMap<Int, Task>?
}