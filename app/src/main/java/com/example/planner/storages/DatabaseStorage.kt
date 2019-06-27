package com.example.planner.storages

import android.content.Context
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import com.example.planner.asyncLoaders.DatabaseLoader
import com.example.planner.asyncLoaders.DatabaseWriter
import com.example.planner.enums.TaskAction
import com.example.planner.enums.TaskKey
import com.example.planner.observer.ErrorObserver
import com.example.planner.observer.StorageObserver
import com.example.planner.task.Task
import java.lang.ref.WeakReference
import java.util.*

const val DATABASE_READ_LOADER_ID = 12
const val DATABASE_EDIT_LOADER_ID = 13
const val DATABASE_REMOVE_LOADER_ID = 14
const val DATABASE_ADD_LOADER_ID = 15

const val DATABASE_READ_TAG = "get"
const val DATABASE_EDIT_TAG = "edit"
const val DATABASE_REMOVE_TAG = "remove"
const val DATABASE_ADD_TAG = "add"

internal object DatabaseStorage : Storage, LoaderManager.LoaderCallbacks<Pair<SortedMap<Int, Task>, String>> {
    private var taskMap = sortedMapOf<Int, Task>()
    private val observers: MutableList<StorageObserver> = ArrayList()

    private var actualObserversGet: MutableList<StorageObserver> = ArrayList()
    private var actualObserversAdd: MutableList<StorageObserver> = ArrayList()
    private var actualObserversEdit: MutableList<StorageObserver> = ArrayList()
    private var actualObserversRemove: MutableList<StorageObserver> = ArrayList()

    private lateinit var context: WeakReference<Context>
    private lateinit var loaderManager: LoaderManager

    fun init(context: WeakReference<Context>, loaderManager: LoaderManager): DatabaseStorage {
        this.context = context
        this.loaderManager = loaderManager

        return this
    }

    override fun onCreateLoader(id: Int, bundle: Bundle?): Loader<Pair<SortedMap<Int, Task>, String>> {
        context.get()?.let {
            return when (id) {
                DATABASE_READ_LOADER_ID -> DatabaseLoader(it)
                else -> DatabaseWriter(
                    it,
                    bundle?.getParcelable(TaskKey.KEY_TASK.getKey()),
                    bundle?.getSerializable(TaskKey.KEY_ACTION.getKey()) as TaskAction,
                    taskMap
                )
            }
        }
        return Loader(context.get()!!)
    }

    override fun onLoadFinished(
        loader: Loader<Pair<SortedMap<Int, Task>, String>>,
        tasks: Pair<SortedMap<Int, Task>, String>?
    ) {
        taskMap = tasks?.first ?: sortedMapOf()

        when (tasks?.second) {
            DATABASE_READ_TAG -> notifyObservers(taskMap, actualObserversGet)
            DATABASE_ADD_TAG -> notifyObservers(taskMap, actualObserversAdd)
            DATABASE_EDIT_TAG -> notifyObservers(taskMap, actualObserversEdit)
            DATABASE_REMOVE_TAG -> notifyObservers(taskMap, actualObserversRemove)
        }
    }

    override fun onLoaderReset(loader: Loader<Pair<SortedMap<Int, Task>, String>>) {
        taskMap = sortedMapOf()
    }

    override fun addTask(task: Task) {
        actualObserversAdd.clear()
        actualObserversAdd.addAll(observers)

        val bundle = Bundle()
        bundle.putParcelable(TaskKey.KEY_TASK.getKey(), task)
        bundle.putSerializable(TaskKey.KEY_ACTION.getKey(), TaskAction.ACTION_ADD)
        loaderManager.restartLoader(DATABASE_ADD_LOADER_ID, bundle, this).forceLoad()
    }

    override fun removeTask(task: Task) {
        actualObserversRemove.clear()
        actualObserversRemove.addAll(observers)

        val bundle = Bundle()
        bundle.putParcelable(TaskKey.KEY_TASK.getKey(), task)
        bundle.putSerializable(TaskKey.KEY_ACTION.getKey(), TaskAction.ACTION_REMOVE)
        loaderManager.restartLoader(DATABASE_REMOVE_LOADER_ID, bundle, this).forceLoad()

    }

    override fun editTask(task: Task) {
        actualObserversEdit.clear()
        actualObserversEdit.addAll(observers)

        val bundle = Bundle()
        bundle.putParcelable(TaskKey.KEY_TASK.getKey(), task)
        bundle.putSerializable(TaskKey.KEY_ACTION.getKey(), TaskAction.ACTION_EDIT)
        loaderManager.restartLoader(DATABASE_EDIT_LOADER_ID, bundle, this).forceLoad()
    }

    override fun getList() {
        actualObserversGet.clear()
        actualObserversGet.addAll(observers)

        if (taskMap.isEmpty()) {
            loaderManager.restartLoader(DATABASE_READ_LOADER_ID, null, this).forceLoad()
        } else {
            notifyObservers(taskMap, actualObserversGet)
        }
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