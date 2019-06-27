package com.example.planner.presenters

import android.content.Context
import android.content.res.Configuration
import android.support.v4.app.LoaderManager
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.planner.enums.TaskAction
import com.example.planner.fragments.StartingPositionChecker
import com.example.planner.observer.ErrorObserver
import com.example.planner.observer.StorageObserver
import com.example.planner.storages.Storage
import com.example.planner.storages.StorageFactory
import com.example.planner.task.Task
import com.example.planner.viewer.MainView

@InjectViewState
class MainPresenter(
    private var context: Context,
    private var loaderManager: LoaderManager
) : StorageObserver, ErrorObserver, MvpPresenter<MainView>() {
    private lateinit var storage: Storage
    private lateinit var typeStorage: Class<Storage>


    fun updateFields(context: Context, loaderManager: LoaderManager) {
        this.context = context
        this.loaderManager = loaderManager
        storage = StorageFactory.getStorage(context, loaderManager)
        typeStorage = storage.javaClass
    }

    override fun onUpdateMap(map: Map<Int, Task>) {
        viewState.onListUpdate(map)

        if (StartingPositionChecker.isNotSetStartPosition) {
            setStartAdapterPosition()
        }

        viewState.checkNotificationDetails()
    }

    override fun showError(error: String, reload: Boolean) {
        viewState.onError(error, reload)
    }

    override fun reloadStorage() {
        viewState.onReloadStorage()
    }

    fun getTasksList() {
        if (storage.javaClass != typeStorage) {
            StartingPositionChecker.isNotSetStartPosition = true
        }

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

    private fun setStartAdapterPosition() {
        if (context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            StartingPositionChecker.isNotSetStartPosition = false
            typeStorage = storage.javaClass
            viewState.setAdapterStartPosition()
        }
    }

    fun updateAdapterPosition(position: Int) {
        StartingPositionChecker.isNotSetStartPosition = false
        StartingPositionChecker.isNotUserSetPosition = false
        viewState.setAdapterSelectedPosition(position)
    }

    fun onStart() {
        storage.addObserver(this)
    }

    fun onStop() {
        storage.removeObserver(this)
    }

    fun onSubscribeError() {
        storage.addErrorObserver(this)
    }

    fun onUnsubscribeError() {
        storage.removeErrorObserver(this)
    }

    fun editTask(task: Task) {
        viewState.editSelectedTask(task)
    }

    fun onNotificationDetails(task: Task) {
        viewState.showDetails(task)
    }
}