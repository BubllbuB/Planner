package com.example.planner.presenters

import android.content.Context
import android.support.v4.app.LoaderManager
import com.example.planner.enums.TaskAction
import com.example.planner.task.Task

interface ITaskPresenter {
    fun updateFields(context: Context, loaderManager: LoaderManager)
    fun updateTask(action: TaskAction, task: Task)
    fun onStart()
    fun onStop()
}