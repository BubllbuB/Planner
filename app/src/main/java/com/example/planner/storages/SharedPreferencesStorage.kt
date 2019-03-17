package com.example.planner.storages

import android.content.Context
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import com.example.planner.asyncLoaders.SharedLoader
import com.example.planner.asyncLoaders.SharedWriter
import com.example.planner.observer.StorageObserver
import com.example.planner.task.Task
import java.lang.ref.WeakReference
import java.util.*


const val SHARED_PREFERENCES_FILE_TASKS = "tasksList"
const val SHARED_PREFERENCES_KEY_TASK = "task_"
const val SHARED_PREFERENCES_KEY_LAST_ID = "lastId"
const val SHARED_LOADER = 0
const val SHARED_EDIT = 2
const val SHARED_REMOVE = 3
const val SHARED_ADD = 4
const val PARCELABLE_TASK = "task"
const val WRITER_ACTION = "action"
const val WRITER_ADD = 1
const val WRITER_REMOVE = 2
const val WRITER_EDIT = 3

object SharedPreferencesStorage : Storage, LoaderManager.LoaderCallbacks<SortedMap<Int, Task>> {
    var taskMap = sortedMapOf<Int, Task>()
    private val observers: MutableList<StorageObserver> = ArrayList()
    private lateinit var context: WeakReference<Context>
    private lateinit var loaderManager: LoaderManager

    fun init(context: WeakReference<Context>, loaderManager: LoaderManager): SharedPreferencesStorage {
        this.context = context
        this.loaderManager = loaderManager

        return this
    }

    override fun onCreateLoader(id: Int, bundle: Bundle?): Loader<SortedMap<Int, Task>> {
        context.get()?.let {
            return when (id) {
                SHARED_LOADER -> SharedLoader(it)
                else -> SharedWriter(
                    it,
                    bundle?.getParcelable(PARCELABLE_TASK),
                    bundle?.getInt(WRITER_ACTION),
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
        bundle.putParcelable(PARCELABLE_TASK, task)
        bundle.putInt(WRITER_ACTION, WRITER_ADD)
        loaderManager.restartLoader(SHARED_ADD, bundle, this).forceLoad()
    }

    override fun removeTask(task: Task) {
        val bundle = Bundle()
        bundle.putParcelable(PARCELABLE_TASK, task)
        bundle.putInt(WRITER_ACTION, WRITER_REMOVE)
        loaderManager.restartLoader(SHARED_REMOVE, bundle, this).forceLoad()
    }

    override fun editTask(task: Task) {
        val bundle = Bundle()
        bundle.putParcelable(PARCELABLE_TASK, task)
        bundle.putInt(WRITER_ACTION, WRITER_EDIT)
        loaderManager.restartLoader(SHARED_EDIT, bundle, this).forceLoad()
    }

    override fun getList() {
        if (taskMap.isEmpty()) {
            loaderManager.restartLoader(SHARED_LOADER, null, this).forceLoad()
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