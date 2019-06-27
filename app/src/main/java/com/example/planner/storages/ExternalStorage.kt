package com.example.planner.storages

import android.content.Context
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import com.example.planner.asyncLoaders.ExternalLoader
import com.example.planner.asyncLoaders.ExternalWriter
import com.example.planner.enums.TaskAction
import com.example.planner.enums.TaskKey
import com.example.planner.observer.ErrorObserver
import com.example.planner.observer.StorageObserver
import com.example.planner.task.Task
import java.lang.ref.WeakReference
import java.util.*

const val EXTERNAL_FILE_TASKS = "tasksList.dat"
const val EXTERNAL_LOADER = 8
const val EXTERNAL_EDIT = 9
const val EXTERNAL_REMOVE = 10
const val EXTERNAL_ADD = 11

const val EXTERNAL_READ_TAG = "get"
const val EXTERNAL_EDIT_TAG = "edit"
const val EXTERNAL_REMOVE_TAG = "remove"
const val EXTERNAL_ADD_TAG = "add"

internal object ExternalStorage : Storage, LoaderManager.LoaderCallbacks<Pair<SortedMap<Int, Task>, String>> {
    private var taskMap = sortedMapOf<Int, Task>()
    private val observers: MutableList<StorageObserver> = ArrayList()

    private var actualObserversGet: MutableList<StorageObserver> = ArrayList()
    private var actualObserversAdd: MutableList<StorageObserver> = ArrayList()
    private var actualObserversEdit: MutableList<StorageObserver> = ArrayList()
    private var actualObserversRemove: MutableList<StorageObserver> = ArrayList()

    private lateinit var context: WeakReference<Context>
    private lateinit var loaderManager: LoaderManager

    fun init(context: WeakReference<Context>, loaderManager: LoaderManager): ExternalStorage {
        this.context = context
        this.loaderManager = loaderManager

        return this
    }

    override fun onCreateLoader(id: Int, bundle: Bundle?): Loader<Pair<SortedMap<Int, Task>, String>> {
        context.get()?.let {
            return when (id) {
                EXTERNAL_LOADER -> ExternalLoader(it)
                else -> ExternalWriter(
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
            EXTERNAL_READ_TAG -> notifyObservers(taskMap, actualObserversGet)
            EXTERNAL_ADD_TAG -> notifyObservers(taskMap, actualObserversAdd)
            EXTERNAL_EDIT_TAG -> notifyObservers(taskMap, actualObserversEdit)
            EXTERNAL_REMOVE_TAG -> notifyObservers(taskMap, actualObserversRemove)
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
        loaderManager.restartLoader(EXTERNAL_ADD, bundle, this).forceLoad()
    }

    override fun removeTask(task: Task) {
        actualObserversRemove.clear()
        actualObserversRemove.addAll(observers)

        val bundle = Bundle()
        bundle.putParcelable(TaskKey.KEY_TASK.getKey(), task)
        bundle.putSerializable(TaskKey.KEY_ACTION.getKey(), TaskAction.ACTION_REMOVE)
        loaderManager.restartLoader(EXTERNAL_REMOVE, bundle, this).forceLoad()

    }

    override fun editTask(task: Task) {
        actualObserversEdit.clear()
        actualObserversEdit.addAll(observers)

        val bundle = Bundle()
        bundle.putParcelable(TaskKey.KEY_TASK.getKey(), task)
        bundle.putSerializable(TaskKey.KEY_ACTION.getKey(), TaskAction.ACTION_EDIT)
        loaderManager.restartLoader(EXTERNAL_EDIT, bundle, this).forceLoad()
    }

    override fun getList() {
        actualObserversGet.clear()
        actualObserversGet.addAll(observers)

        if (taskMap.isEmpty()) {
            loaderManager.restartLoader(EXTERNAL_LOADER, null, this).forceLoad()
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