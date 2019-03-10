package com.example.planner.presenters

import android.content.res.Resources
import com.example.planner.R
import com.example.planner.storages.Storage
import com.example.planner.storages.StorageFactory
import com.example.planner.task.Task
import com.example.planner.viewer.MainView

class MainPresenter(private val view: MainView, private val resources: Resources) {
    private val storage: Storage = StorageFactory.getStorage()

    fun onUpdaterList() {
        view.onListUpdate(storage.getList())
    }

    fun updateTask(actionId: Int, task: Task?) {
        when (actionId) {
            resources.getInteger(R.integer.taskAdd) -> storage.addTask(task)
            resources.getInteger(R.integer.taskEdit) -> storage.editTask(task)
            resources.getInteger(R.integer.taskRemove) -> storage.removeTask(task)
            resources.getInteger(R.integer.setFavorite) -> storage.editTask(task)
        }
        onUpdaterList()
    }

    fun editTask(task: Task?) {
        view.editSelectedTask(task)
    }
}