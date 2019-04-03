package com.example.planner.presenters

import android.content.Context
import android.support.v4.app.LoaderManager
import com.example.planner.enums.TaskAction
import com.example.planner.observer.StorageObserver
import com.example.planner.storages.Storage
import com.example.planner.storages.StorageFactory
import com.example.planner.task.Task
import com.example.planner.viewer.MainView

class MainPresenter(
    private val view: MainView,
    private var context: Context,
    private var loaderManager: LoaderManager
) : StorageObserver, IMainPresenter {
    private var storage: Storage = StorageFactory.getStorage(context, loaderManager)

    override fun updateFields(context: Context, loaderManager: LoaderManager) {
        this.context = context
        this.loaderManager = loaderManager
        this.storage = StorageFactory.getStorage(context, loaderManager)
    }

    override fun onUpdateMap(map: Map<Int, Task>) {
        view.onListUpdate(map)
    }

    override fun getTasksList() {
        view.showProgressBars()
        storage.getList()
    }

    override fun updateTask(action: TaskAction, task: Task) {
        view.showProgressBars()

        when (action) {
            TaskAction.ACTION_ADD -> storage.addTask(task)
            TaskAction.ACTION_EDIT -> storage.editTask(task)
            TaskAction.ACTION_REMOVE -> storage.removeTask(task)
            TaskAction.ACTION_FAVORITE -> storage.editTask(task)
            TaskAction.ACTION_DONE -> storage.editTask(task)
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