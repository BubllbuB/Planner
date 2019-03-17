package com.example.planner.storages

import com.example.planner.observer.StorageObserver
import com.example.planner.task.Task

object CacheStorage: Storage {
    private var tasksList = sortedMapOf<Int,Task>()
    private val observers: MutableList<StorageObserver> = ArrayList()

    override fun addTask(task: Task) {
        task.id = tasksList.size+1
        tasksList[task.id] = task
        notifyObservers(tasksList)
    }

    override fun removeTask(task: Task) {
        tasksList.remove(task.id)
        notifyObservers(tasksList)
    }

    override fun editTask(task: Task) {
        tasksList[task.id] = task
        notifyObservers(tasksList)
    }

    override fun getList() {
        notifyObservers(tasksList)
    }

    override fun addObserver(observer: StorageObserver) {
        observers.add(observer)
    }

    override fun removeObserver(observer: StorageObserver) {
        observers.remove(observer)
    }

    override fun notifyObservers(tasks: Map<Int,Task>) {
        observers.forEach { it.onUpdateList(tasks) }
    }
}