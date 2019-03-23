package com.example.planner.viewer

import com.example.planner.task.Task

interface MainView {
    fun onListUpdate(tasks: Map<Int, Task>)
    fun editSelectedTask(task: Task?)
    fun showProgressBars()
}