package com.example.planner.presenters

import android.support.v4.app.LoaderManager
import android.support.v7.app.AppCompatActivity
import com.example.planner.R
import com.example.planner.storages.Storage
import com.example.planner.storages.StorageFactory
import com.example.planner.task.Task

class TaskPresenter(private val view: AppCompatActivity, loaderManager: LoaderManager) {
    private val storage: Storage = StorageFactory.getStorage(view.baseContext, loaderManager)

    fun updateTask(actionId: Int, task: Task?) {
        when (actionId) {
            view.applicationContext.resources.getInteger(R.integer.taskAdd) -> storage.addTask(task)
            view.applicationContext.resources.getInteger(R.integer.taskEdit) -> storage.editTask(task)
        }
    }
}