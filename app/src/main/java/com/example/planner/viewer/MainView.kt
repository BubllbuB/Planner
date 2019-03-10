package com.example.planner.viewer

import com.example.planner.task.Task
import java.util.*

interface MainView {
    fun onListUpdate(tasks: SortedMap<Int, Task>?)
    fun editSelectedTask(task: Task?)
}