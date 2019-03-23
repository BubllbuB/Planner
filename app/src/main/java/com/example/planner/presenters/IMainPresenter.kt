package com.example.planner.presenters

import android.content.Context
import android.support.v4.app.LoaderManager
import com.example.planner.task.Task

interface IMainPresenter {
    fun updateFields(context: Context, loaderManager: LoaderManager)
    fun getTasksList()
    fun updateTask(actionId: Int, task: Task)
    fun onStart()
    fun onStop()
    fun editTask(task: Task)
}