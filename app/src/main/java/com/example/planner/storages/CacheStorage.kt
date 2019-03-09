package com.example.planner.storages

import com.example.planner.task.Task

object CacheStorage: Storage {
    private var tasksList = arrayListOf<Task>()

    override fun addTask(task: Task?) {
        task?.id = tasksList.size+1
        task?.let {
            tasksList.add(task)
        }

    }

    override fun removeTask(task: Task?) {
        tasksList.remove(task)
    }

    override fun editTask(task: Task?) {
        task?.let {
            tasksList[task.id] = task
        }
    }

    override fun getList(): ArrayList<Task>? {
        return tasksList
    }
}