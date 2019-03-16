package com.example.planner.presenters

import android.content.Context
import android.content.res.Resources
import android.support.v4.app.LoaderManager
import com.example.planner.R
import com.example.planner.storages.Storage
import com.example.planner.storages.StorageFactory
import com.example.planner.task.Task
import com.example.planner.viewer.MainView
import java.util.*

class MainPresenter(private val view: MainView, context: Context, loaderManager: LoaderManager, private val resources: Resources) {
    private val storage: Storage = StorageFactory.getStorage(context, loaderManager)

    fun onUpdaterList(tasks: SortedMap<Int, Task>) {
        view.onListUpdate(tasks)
    }

    fun getTasksList() {
        storage.getList(this)
    }

    fun updateTask(actionId: Int, task: Task?) {
        view.showProgressBars()

        when (actionId) {
            resources.getInteger(R.integer.taskAdd) -> storage.addTask(task)
            resources.getInteger(R.integer.taskEdit) -> storage.editTask(task)
            resources.getInteger(R.integer.taskRemove) -> storage.removeTask(task)
            resources.getInteger(R.integer.setFavorite) -> storage.editTask(task)
            resources.getInteger(R.integer.setDone) -> storage.editTask(task)
        }
    }

    fun editTask(task: Task?) {
        view.editSelectedTask(task)
    }
}