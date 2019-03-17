package com.example.planner.presenters

import com.example.planner.task.Task

interface IMainPresenter {
    fun getTasksList()
    fun updateTask(actionId: Int, task: Task)
    fun startListenStorage()
    fun stopListenStorage()
    fun editTask(task: Task)
}