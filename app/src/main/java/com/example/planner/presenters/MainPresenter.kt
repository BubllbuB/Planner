package com.example.planner.presenters

import android.content.Context
import android.support.v4.app.LoaderManager
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.planner.enums.TaskAction
import com.example.planner.observer.StorageObserver
import com.example.planner.storages.Storage
import com.example.planner.storages.StorageFactory
import com.example.planner.task.Task
import com.example.planner.viewer.MainView

@InjectViewState
class MainPresenter(
    private var context: Context,
    private var loaderManager: LoaderManager
) : StorageObserver, MvpPresenter<MainView>() {
    private var storage: Storage = StorageFactory.getStorage(context, loaderManager)

    fun updateFields(context: Context, loaderManager: LoaderManager) {
        this.context = context
        this.loaderManager = loaderManager
        this.storage = StorageFactory.getStorage(context, loaderManager)
    }

    override fun onUpdateMap(map: Map<Int, Task>) {
        viewState.onListUpdate(map)
    }

    fun getTasksList() {
        viewState.showProgressBars()
        storage.getList()
    }

    fun updateTask(action: TaskAction, task: Task) {
        viewState.showProgressBars()

        when (action) {
            TaskAction.ACTION_ADD -> storage.addTask(task)
            TaskAction.ACTION_EDIT -> storage.editTask(task)
            TaskAction.ACTION_REMOVE -> storage.removeTask(task)
            TaskAction.ACTION_FAVORITE -> storage.editTask(task)
            TaskAction.ACTION_DONE -> storage.editTask(task)
        }
    }

    fun updateAdapterPosition(position: Int) {
        viewState.setAdapterSelectedPosition(position)
    }

    fun onStart() {
        storage.addObserver(this)
    }

    fun onStop() {
        storage.removeObserver(this)
    }

    fun editTask(task: Task) {
        viewState.editSelectedTask(task)
    }
}