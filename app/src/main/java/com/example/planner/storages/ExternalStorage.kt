package com.example.planner.storages

import android.content.Context
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import com.example.planner.asyncLoaders.ExternalLoader
import com.example.planner.asyncLoaders.ExternalWriter
import com.example.planner.observer.StorageObserver
import com.example.planner.task.Task
import java.lang.ref.WeakReference
import java.util.*

const val EXTERNAL_FILE_TASKS = "tasksList.dat"
const val EXTERNAL_LOADER = 0
const val EXTERNAL_EDIT = 2
const val EXTERNAL_REMOVE = 3
const val EXTERNAL_ADD = 4
const val EXTERNAL_PARCELABLE_TASK = "task"
const val EXTERNAL_WRITER_ACTION = "action"
const val EXTERNAL_WRITER_ADD = 1
const val EXTERNAL_WRITER_REMOVE = 2
const val EXTERNAL_WRITER_EDIT = 3

object ExternalStorage : Storage, LoaderManager.LoaderCallbacks<SortedMap<Int, Task>> {
    var taskMap = sortedMapOf<Int, Task>()
    private val observers: MutableList<StorageObserver> = ArrayList()

    private lateinit var context: WeakReference<Context>
    private lateinit var loaderManager: LoaderManager

    fun init(context: WeakReference<Context>, loaderManager: LoaderManager): ExternalStorage {
        this.context = context
        this.loaderManager = loaderManager

        return this
    }

    override fun onCreateLoader(id: Int, bundle: Bundle?): Loader<SortedMap<Int, Task>> {
        context.get()?.let {
            return when (id) {
                EXTERNAL_LOADER -> ExternalLoader(it)
                else -> ExternalWriter(
                    it,
                    bundle?.getParcelable(EXTERNAL_PARCELABLE_TASK),
                    bundle?.getInt(EXTERNAL_WRITER_ACTION),
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
        bundle.putParcelable(EXTERNAL_PARCELABLE_TASK, task)
        bundle.putInt(EXTERNAL_WRITER_ACTION, EXTERNAL_WRITER_ADD)
        loaderManager.restartLoader(EXTERNAL_ADD, bundle, this).forceLoad()
    }

    override fun removeTask(task: Task) {
        val bundle = Bundle()
        bundle.putParcelable(PARCELABLE_TASK, task)
        bundle.putInt(EXTERNAL_WRITER_ACTION, EXTERNAL_WRITER_REMOVE)
        loaderManager.restartLoader(EXTERNAL_REMOVE, bundle, this).forceLoad()

    }

    override fun editTask(task: Task) {
        val bundle = Bundle()
        bundle.putParcelable(PARCELABLE_TASK, task)
        bundle.putInt(EXTERNAL_WRITER_ACTION, EXTERNAL_WRITER_EDIT)
        loaderManager.restartLoader(EXTERNAL_EDIT, bundle, this).forceLoad()
    }

    override fun getList() {
        if (taskMap.isEmpty()) {
            loaderManager.restartLoader(EXTERNAL_LOADER, null, this).forceLoad()
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