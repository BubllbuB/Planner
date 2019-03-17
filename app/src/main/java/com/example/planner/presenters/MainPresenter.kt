package com.example.planner.presenters

import android.content.Context
import android.content.res.Resources
import com.example.planner.R
import com.example.planner.observer.StorageObserver
import com.example.planner.storages.CacheStorage
import com.example.planner.storages.Storage
import com.example.planner.storages.StorageFactory
import com.example.planner.task.Task
import com.example.planner.viewer.MainView

class MainPresenter(private val view: MainView, context: Context, private val resources: Resources): StorageObserver, IMainPresenter {
    private val storage: Storage = StorageFactory.getStorage(context)


    override fun onUpdateList(list: Map<Int,Task>) {
        view.onListUpdate(list)
    }

    override fun getTasksList() {
        storage.getList()
    }

    override fun updateTask(actionId: Int, task: Task) {
        when (actionId) {
            resources.getInteger(R.integer.taskAdd) -> storage.addTask(task)
            resources.getInteger(R.integer.taskEdit) -> storage.editTask(task)
            resources.getInteger(R.integer.taskRemove) -> storage.removeTask(task)
            resources.getInteger(R.integer.setFavorite) -> storage.editTask(task)
            resources.getInteger(R.integer.setDone) -> storage.editTask(task)
        }
    }

    override fun startListenStorage() {
        storage.addObserver(this)
    }

    override fun stopListenStorage() {
        storage.removeObserver(this)
    }

    override fun editTask(task: Task) {
        view.editSelectedTask(task)
    }
}