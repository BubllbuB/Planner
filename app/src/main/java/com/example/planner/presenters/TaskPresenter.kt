package com.example.planner.presenters

import android.support.v7.app.AppCompatActivity
import com.example.planner.R
import com.example.planner.storages.CacheStorage
import com.example.planner.storages.Storage
import com.example.planner.task.Task

class TaskPresenter(private val view: AppCompatActivity) {
    private val storage: Storage = CacheStorage

    fun updateTask(actionId: Int, task: Task?) {
        when (actionId) {
            view.applicationContext.resources.getInteger(R.integer.taskAdd) -> storage.addTask(task)
            view.applicationContext.resources.getInteger(R.integer.taskEdit) -> storage.editTask(task)
        }
    }
}