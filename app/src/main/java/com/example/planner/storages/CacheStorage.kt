package com.example.planner.storages

import com.example.planner.presenters.MainPresenter
import com.example.planner.task.Task
import java.util.*

object CacheStorage: Storage {
    private var tasksList = sortedMapOf<Int,Task>()
    private var taskId = 0
    private lateinit var presenterView: MainPresenter

    override fun addTask(task: Task?) {
        task?.id = taskId++
        tasksList[task?.id] = task
        onPresenterUpdate()
    }

    override fun removeTask(task: Task?) {
        tasksList.remove(task?.id)
        onPresenterUpdate()
    }

    override fun editTask(task: Task?) {
        tasksList[task?.id] = task
        onPresenterUpdate()
    }

    override fun getList(presenter: MainPresenter): SortedMap<Int, Task>? {
        this.presenterView = presenter
        onPresenterUpdate()
        return tasksList
    }

    private fun onPresenterUpdate() {
        presenterView.onUpdaterList(tasksList)
    }
}