package com.example.planner.presenters

import android.content.Context
import android.support.v4.app.LoaderManager
import com.example.planner.enums.TaskAction
import com.example.planner.observer.StorageObserver
import com.example.planner.storages.Storage
import com.example.planner.storages.StorageFactory
import com.example.planner.task.Task
import com.example.planner.viewer.AddView

class TaskPresenter(private val view: AddView, private var context: Context, private var loaderManager: LoaderManager) :
    StorageObserver,
    ITaskPresenter {
    private var storage: Storage = StorageFactory.getStorage(context, loaderManager)

    override fun updateFields(context: Context, loaderManager: LoaderManager) {
        this.context = context
        this.loaderManager = loaderManager
        this.storage = StorageFactory.getStorage(context, loaderManager)
    }

    override fun onUpdateMap(map: Map<Int, Task>) {
        view.onTaskSaveSuccess()
    }

    override fun updateTask(action: TaskAction, task: Task) {
        when (action) {
            TaskAction.ACTION_ADD -> storage.addTask(task)
            else -> storage.editTask(task)
        }
    }

    override fun onStart() {
        storage.addObserver(this)
    }

    override fun onStop() {
        storage.removeObserver(this)
    }
}