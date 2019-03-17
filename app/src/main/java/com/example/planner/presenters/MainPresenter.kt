package com.example.planner.presenters

import android.content.Context
import android.content.res.Resources
import android.support.v4.app.LoaderManager
import com.example.planner.R
import com.example.planner.observer.StorageObserver
import com.example.planner.storages.Storage
import com.example.planner.storages.StorageFactory
import com.example.planner.task.Task
import com.example.planner.viewer.MainView

class MainPresenter(
    private val view: MainView,
    context: Context,
    loaderManager: LoaderManager,
    private val resources: Resources
) : StorageObserver, IMainPresenter {
    private val storage: Storage = StorageFactory.getStorage(context, loaderManager)


    override fun onUpdateList(list: Map<Int, Task>) {
        view.onListUpdate(list)
    }

    override fun getTasksList() {
        storage.getList()
    }

    override fun updateTask(actionId: Int, task: Task) {
        view.showProgressBars()

        when (actionId) {
            resources.getInteger(R.integer.taskAdd) -> storage.addTask(task)
            resources.getInteger(R.integer.taskEdit) -> storage.editTask(task)
            resources.getInteger(R.integer.taskRemove) -> storage.removeTask(task)
            resources.getInteger(R.integer.setFavorite) -> storage.editTask(task)
            resources.getInteger(R.integer.setDone) -> storage.editTask(task)
        }
    }

    override fun onStart() {
        storage.addObserver(this)
    }

    override fun onStop() {
        storage.removeObserver(this)
    }

    override fun editTask(task: Task) {
        view.editSelectedTask(task)
    }
}