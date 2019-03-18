package com.example.planner.presenters

import com.example.planner.task.Task

interface ITaskPresenter {
    fun updateTask(actionId: Int, task: Task)
    fun startListenStorage()
    fun stopListenStorage()
}