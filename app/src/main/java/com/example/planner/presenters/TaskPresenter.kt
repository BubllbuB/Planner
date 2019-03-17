package com.example.planner.presenters

import com.example.planner.observer.StorageObserver
import com.example.planner.storages.CacheStorage
import com.example.planner.storages.Storage
import com.example.planner.task.Task
import com.example.planner.viewer.AddView

const val TASK_ADD = 1
const val TASK_EDIT = 2

class TaskPresenter(private val view: AddView) : ITaskPresenter, StorageObserver {
    private val storage: Storage = CacheStorage

    override fun onUpdateList(list: Map<Int,Task>) {
        view.onTaskSaveSuccess()
    }

    override fun updateTask(actionId: Int, task: Task) {
        when (actionId) {
            TASK_ADD -> storage.addTask(task)
            TASK_EDIT -> storage.editTask(task)
        }
    }

    override fun startListenStorage() {
        storage.addObserver(this)
    }

    override fun stopListenStorage() {
        storage.removeObserver(this)
    }
}