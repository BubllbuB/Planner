package com.example.planner.presenters

import android.content.Context
import android.support.v4.app.LoaderManager
import com.example.planner.observer.StorageObserver
import com.example.planner.storages.Storage
import com.example.planner.storages.StorageFactory
import com.example.planner.task.Task
import com.example.planner.viewer.MainView

const val TASK_REMOVE = 3
const val TASK_FAVORITE = 4
const val TASK_DONE = 5

class MainPresenter(
    private val view: MainView,
    context: Context,
    loaderManager: LoaderManager
) : StorageObserver, IMainPresenter {
    private val storage: Storage = StorageFactory.getStorage(context, loaderManager)


    override fun onUpdateList(list: Map<Int, Task>) {
        view.onListUpdate(list)
    }

    override fun getTasksList() {
        view.showProgressBars()
        storage.getList()
    }

    override fun updateTask(actionId: Int, task: Task) {
        view.showProgressBars()

        when (actionId) {
            TASK_ADD -> storage.addTask(task)
            TASK_EDIT -> storage.editTask(task)
            TASK_REMOVE -> storage.removeTask(task)
            TASK_FAVORITE -> storage.editTask(task)
            TASK_DONE -> storage.editTask(task)
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