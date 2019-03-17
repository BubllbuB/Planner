package com.example.planner.storages

import android.content.Context
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import com.example.planner.asyncLoaders.InternalLoader
import com.example.planner.asyncLoaders.InternalWriter
import com.example.planner.observer.StorageObserver
import com.example.planner.task.Task
import java.lang.ref.WeakReference
import java.util.*

const val INTERNAL_FILE_TASKS = "tasksList.dat"
const val INTERNAL_LOADER = 0
const val INTERNAL_EDIT = 2
const val INTERNAL_REMOVE = 3
const val INTERNAL_ADD = 4
const val INTERNAL_PARCELABLE_TASK = "task"
const val INTERNAL_WRITER_ACTION = "action"
const val INTERNAL_WRITER_ADD = 1
const val INTERNAL_WRITER_REMOVE = 2
const val INTERNAL_WRITER_EDIT = 3

object InternalStorage : Storage, LoaderManager.LoaderCallbacks<SortedMap<Int, Task>> {
    var taskMap = sortedMapOf<Int, Task>()
    private val observers: MutableList<StorageObserver> = ArrayList()

    private lateinit var context: WeakReference<Context>
    private lateinit var loaderManager: LoaderManager

    fun init(context: WeakReference<Context>, loaderManager: LoaderManager): InternalStorage {
        this.context = context
        this.loaderManager = loaderManager
        return this
    }

    override fun onCreateLoader(id: Int, bundle: Bundle?): Loader<SortedMap<Int, Task>> {
        context.get()?.let {
            return when (id) {
                INTERNAL_LOADER -> InternalLoader(it)
                else -> InternalWriter(
                    it,
                    bundle?.getParcelable(INTERNAL_PARCELABLE_TASK),
                    bundle?.getInt(INTERNAL_WRITER_ACTION),
                    taskMap
                )
            }
        }
        return Loader(context.get()!!)
    }

    override fun onLoadFinished(loader: Loader<SortedMap<Int, Task>>, tasks: SortedMap<Int, Task>?) {
        taskMap = tasks ?: sortedMapOf()
        notifyObservers(taskMap)
    }

    override fun onLoaderReset(loader: Loader<SortedMap<Int, Task>>) {
        taskMap = sortedMapOf()
    }

    override fun addTask(task: Task) {
        val bundle = Bundle()
        bundle.putParcelable(INTERNAL_PARCELABLE_TASK, task)
        bundle.putInt(INTERNAL_WRITER_ACTION, INTERNAL_WRITER_ADD)
        loaderManager.restartLoader(INTERNAL_ADD, bundle, this).forceLoad()
    }

    override fun removeTask(task: Task) {
        val bundle = Bundle()
        bundle.putParcelable(PARCELABLE_TASK, task)
        bundle.putInt(INTERNAL_WRITER_ACTION, INTERNAL_WRITER_REMOVE)
        loaderManager.restartLoader(INTERNAL_REMOVE, bundle, this).forceLoad()

    }

    override fun editTask(task: Task) {
        val bundle = Bundle()
        bundle.putParcelable(PARCELABLE_TASK, task)
        bundle.putInt(INTERNAL_WRITER_ACTION, INTERNAL_WRITER_EDIT)
        loaderManager.restartLoader(INTERNAL_EDIT, bundle, this).forceLoad()
    }

    override fun getList() {
        if (taskMap.isEmpty()) {
            loaderManager.initLoader(INTERNAL_LOADER, null, this).forceLoad()
        } else {
            notifyObservers(taskMap)
        }
    }

    override fun addObserver(observer: StorageObserver) {
        observers.add(observer)
    }

    override fun removeObserver(observer: StorageObserver) {
        observers.remove(observer)
    }

    override fun notifyObservers(tasks: Map<Int, Task>) {
        observers.forEach { it.onUpdateList(tasks) }
    }
}