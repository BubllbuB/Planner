package com.example.planner.storages

import com.example.planner.observer.ErrorObserver
import com.example.planner.observer.StorageObserver
import com.example.planner.task.Task
import java.util.*

internal object CacheStorage : Storage {
    private var tasksList = sortedMapOf<Int, Task>()
    private var taskId = 0
    private val observers: MutableList<StorageObserver> = ArrayList()

    private var actualObserversGet: MutableList<StorageObserver> = ArrayList()
    private var actualObserversAdd: MutableList<StorageObserver> = ArrayList()
    private var actualObserversEdit: MutableList<StorageObserver> = ArrayList()
    private var actualObserversRemove: MutableList<StorageObserver> = ArrayList()

    override fun addTask(task: Task) {
        actualObserversAdd.clear()
        actualObserversAdd.addAll(observers)

        task.id = taskId++
        tasksList[task.id] = task
        notifyObservers(tasksList, actualObserversAdd)
    }

    override fun removeTask(task: Task) {
        actualObserversRemove.clear()
        actualObserversRemove.addAll(observers)

        tasksList.remove(task.id)
        notifyObservers(tasksList, actualObserversRemove)
    }

    override fun editTask(task: Task) {
        actualObserversEdit.clear()
        actualObserversEdit.addAll(observers)

        tasksList[task.id] = task
        notifyObservers(tasksList, actualObserversEdit)
    }

    override fun getList() {
        actualObserversGet.clear()
        actualObserversGet.addAll(observers)

        notifyObservers(tasksList, actualObserversGet)
    }

    override fun addObserver(observer: StorageObserver) {
        observers.add(observer)
    }

    override fun removeObserver(observer: StorageObserver) {
        observers.remove(observer)
        actualObserversGet.remove(observer)
        actualObserversAdd.remove(observer)
        actualObserversEdit.remove(observer)
        actualObserversRemove.remove(observer)
    }

    private fun notifyObservers(tasks: Map<Int, Task>, observers: MutableList<StorageObserver>) {
        observers.forEach { it.onUpdateMap(tasks) }
    }

    override fun addErrorObserver(observer: ErrorObserver) {
    }

    override fun removeErrorObserver(observer: ErrorObserver) {
    }
}