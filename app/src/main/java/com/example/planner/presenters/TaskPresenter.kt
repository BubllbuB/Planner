package com.example.planner.presenters

import android.content.Context
import android.content.res.Configuration
import android.support.v4.app.LoaderManager
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.planner.enums.TaskAction
import com.example.planner.observer.StorageObserver
import com.example.planner.storages.Storage
import com.example.planner.storages.StorageFactory
import com.example.planner.task.Task
import com.example.planner.viewer.AddView

@InjectViewState
class TaskPresenter(private var context: Context, private var loaderManager: LoaderManager) :
    StorageObserver,
    MvpPresenter<AddView>() {
    private var storage: Storage = StorageFactory.getStorage(context, loaderManager)
    private var userSelected = false

    fun updateFields(context: Context, loaderManager: LoaderManager) {
        this.context = context
        this.loaderManager = loaderManager
        this.storage = StorageFactory.getStorage(context, loaderManager)
    }

    override fun onUpdateMap(map: Map<Int, Task>) {
        viewState.onTaskSaveSuccess()
    }

    fun updateTask(action: TaskAction, task: Task) {
        when (action) {
            TaskAction.ACTION_ADD -> storage.addTask(task)
            else -> storage.editTask(task)
        }
    }

    fun onStart() {
        storage.addObserver(this)
    }

    fun onStop() {
        storage.removeObserver(this)
    }

    fun onSetTitle(title: String, startPos: Int, endPos: Int) {
        viewState.setTitle(title, startPos, endPos)
    }

    fun onSetDescription(title: String, startPos: Int, endPos: Int) {
        viewState.setDescription(title, startPos, endPos)
    }

    fun onSetFocus(focusId: Int) {
        if (focusId > 0) userSelected = true
        viewState.setFocus(focusId)
    }

    fun onRestore() {
        if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT && userSelected) {
            viewState.replaceFragment()
        } else if(context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            viewState.addFragment()
        }
    }
}