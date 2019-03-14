package com.example.planner.storages

import com.example.planner.presenters.MainPresenter
import com.example.planner.task.Task
import java.util.*

object CacheStorage: Storage {
    private var tasksList = sortedMapOf<Int,Task>()
    private var taskId = 0

    override fun addTask(task: Task?) {
        task?.id = taskId++
        tasksList[task?.id] = task
    }

    override fun removeTask(task: Task?) {
        tasksList.remove(task?.id)
    }

    override fun editTask(task: Task?) {
        tasksList[task?.id] = task
    }

    override fun getList(presenter: MainPresenter): SortedMap<Int, Task>? {
        return tasksList
    }
}