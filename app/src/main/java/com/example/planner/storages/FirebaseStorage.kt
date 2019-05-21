package com.example.planner.storages

import android.content.Context
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import com.example.planner.asyncLoaders.DatabaseLoader
import com.example.planner.asyncLoaders.DatabaseWriter
import com.example.planner.enums.TaskAction
import com.example.planner.enums.TaskKey
import com.example.planner.observer.StorageObserver
import com.example.planner.task.Task
import java.lang.ref.WeakReference
import java.util.*

const val FIREBASE_READ_LOADER_ID = 16
const val FIREBASE_EDIT_LOADER_ID = 17
const val FIREBASE_REMOVE_LOADER_ID = 18
const val FIREBASE_ADD_LOADER_ID = 19

internal object FirebaseStorage : Storage, LoaderManager.LoaderCallbacks<SortedMap<Int, Task>> {
    private var taskMap = sortedMapOf<Int, Task>()
    private val observers: MutableList<StorageObserver> = ArrayList()

    private lateinit var context: WeakReference<Context>
    private lateinit var loaderManager: LoaderManager

    fun init(context: WeakReference<Context>, loaderManager: LoaderManager): FirebaseStorage {
        this.context = context
        this.loaderManager = loaderManager

        return this
    }

    override fun onCreateLoader(id: Int, bundle: Bundle?): Loader<SortedMap<Int, Task>> {

        return Loader(context.get()!!)
    }

    override fun onLoadFinished(loader: Loader<SortedMap<Int, Task>>, tasks: SortedMap<Int, Task>?) {

    }

    override fun onLoaderReset(loader: Loader<SortedMap<Int, Task>>) {

    }

    override fun addTask(task: Task) {
        val bundle = Bundle()
        bundle.putParcelable(TaskKey.KEY_TASK.getKey(), task)
        bundle.putSerializable(TaskKey.KEY_ACTION.getKey(), TaskAction.ACTION_ADD)
        loaderManager.restartLoader(DATABASE_ADD_LOADER_ID, bundle, this).forceLoad()
    }

    override fun removeTask(task: Task) {

    }

    override fun editTask(task: Task) {

    }

    override fun getList() {

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