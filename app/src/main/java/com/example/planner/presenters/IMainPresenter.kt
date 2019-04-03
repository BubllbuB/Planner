package com.example.planner.presenters

import android.content.Context
import android.support.v4.app.LoaderManager
import com.example.planner.enums.TaskAction
import com.example.planner.task.Task
import java.io.Serializable

interface IMainPresenter: Serializable {
    fun updateFields(context: Context, loaderManager: LoaderManager)
    fun getTasksList()
    fun updateTask(action: TaskAction, task: Task)
    fun onStart()
    fun onStop()
    fun editTask(task: Task)
}