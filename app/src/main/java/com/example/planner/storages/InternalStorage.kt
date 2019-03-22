package com.example.planner.storages

import android.content.Context
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import com.example.planner.asyncLoaders.InternalLoader
import com.example.planner.asyncLoaders.InternalWriter
import com.example.planner.enums.TaskActionId
import com.example.planner.enums.TaskKey
import com.example.planner.observer.StorageObserver
import com.example.planner.task.Task
import java.lang.ref.WeakReference
import java.util.*

const val INTERNAL_FILE_TASKS = "tasksList.dat"
const val INTERNAL_LOADER = 4
const val INTERNAL_EDIT = 5
const val INTERNAL_REMOVE = 6
const val INTERNAL_ADD = 7

object InternalStorage : Storage, LoaderManager.LoaderCallbacks<SortedMap<Int, Task>> {
    private var taskMap = sortedMapOf<Int, Task>()
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
                    bundle?.getParcelable(TaskKey.KEY_TASK.getKey()),
                    bundle?.getInt(TaskKey.KEY_ACTION.getKey()),
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
        bundle.putParcelable(TaskKey.KEY_TASK.getKey(), task)
        bundle.putInt(TaskKey.KEY_ACTION.getKey(), TaskActionId.ACTION_ADD.getId())
        loaderManager.restartLoader(INTERNAL_ADD, bundle, this).forceLoad()
    }

    override fun removeTask(task: Task) {
        val bundle = Bundle()
        bundle.putParcelable(TaskKey.KEY_TASK.getKey(), task)
        bundle.putInt(TaskKey.KEY_ACTION.getKey(), TaskActionId.ACTION_REMOVE.getId())
        loaderManager.restartLoader(INTERNAL_REMOVE, bundle, this).forceLoad()

    }

    override fun editTask(task: Task) {
        val bundle = Bundle()
        bundle.putParcelable(TaskKey.KEY_TASK.getKey(), task)
        bundle.putInt(TaskKey.KEY_ACTION.getKey(), TaskActionId.ACTION_EDIT.getId())
        loaderManager.restartLoader(INTERNAL_EDIT, bundle, this).forceLoad()
    }

    override fun getList() {
        if (taskMap.isEmpty()) {
            loaderManager.restartLoader(INTERNAL_LOADER, null, this).forceLoad()
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

    private fun notifyObservers(tasks: Map<Int, Task>) {
        observers.forEach { it.onUpdateMap(tasks) }
    }
}