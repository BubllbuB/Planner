package com.example.planner.storages

import android.content.Context
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import com.example.planner.asyncLoaders.SharedLoader
import com.example.planner.asyncLoaders.SharedWriter
import com.example.planner.presenters.MainPresenter
import com.example.planner.task.Task
import com.google.gson.Gson
import java.util.*


const val SHARED_PREFERENCES_FILE_TASKS = "tasksList"
const val SHARED_PREFERENCES_KEY_TASK_LIST = "tasks"
const val SHARED_PREFERENCES_KEY_LAST_ID = "lastId"
const val SHARED_LOADER = 0
const val SHARED_WRITER = 1
const val PARCEBLE_TASK = "task"

class SharedPreferencesStorage(
    private val context: Context,
    private val loaderManager: LoaderManager,
    private val tasksList: SortedMap<Int, Task>
) : Storage, LoaderManager.LoaderCallbacks<SortedMap<Int, Task>> {
    private var gson = Gson()
    var taskListJsonString = ""
    var taskMap = sortedMapOf<Int, Task>()
    private lateinit var presenterView: MainPresenter

    override fun onCreateLoader(id: Int, bundle: Bundle?): Loader<SortedMap<Int, Task>> {
        return when(id) {
            SHARED_LOADER -> SharedLoader(context)
            SHARED_WRITER -> SharedWriter(context, bundle?.getString(PARCEBLE_TASK))
            else -> Loader(context)
        }
    }

    override fun onLoadFinished(loader: Loader<SortedMap<Int, Task>>, tasks: SortedMap<Int, Task>?) {
        if(loader is SharedLoader) {
            taskMap = tasks ?: sortedMapOf()
            presenterView.onUpdaterList(taskMap)
        }
    }

    override fun onLoaderReset(loader: Loader<SortedMap<Int, Task>>) {
        taskMap = sortedMapOf()
    }

    override fun addTask(task: Task?) {
        val bundle = Bundle()
        val sharedFile = context.getSharedPreferences(SHARED_PREFERENCES_FILE_TASKS, 0)
        val lastId = sharedFile.getInt(SHARED_PREFERENCES_KEY_LAST_ID, 0)

        task?.id = lastId + 1
        tasksList[task?.id] = task

        taskListJsonString = gson.toJson(tasksList)

        bundle.putString(PARCEBLE_TASK, taskListJsonString)
        loaderManager.initLoader(SHARED_WRITER, bundle, this).forceLoad()


    }

    override fun removeTask(task: Task?) {
        val sharedFile = context.getSharedPreferences(SHARED_PREFERENCES_FILE_TASKS, 0)
        val editor = sharedFile.edit()

        //val loader = SharedLoader(context.getSharedPreferences(SHARED_PREFERENCES_FILE_TASKS, 0))
        //tasksList = loader.execute().get()

        tasksList.remove(task?.id)

        taskListJsonString = gson.toJson(tasksList)
        editor.putString(SHARED_PREFERENCES_KEY_TASK_LIST, taskListJsonString)
        editor.apply()
    }

    override fun editTask(task: Task?) {
        val sharedFile = context.getSharedPreferences(SHARED_PREFERENCES_FILE_TASKS, 0)
        val editor = sharedFile.edit()

        //val loader = SharedLoader(context.getSharedPreferences(SHARED_PREFERENCES_FILE_TASKS, 0))
        //tasksList = loader.execute().get()

        tasksList[task?.id] = task

        taskListJsonString = gson.toJson(tasksList)
        editor.putString(SHARED_PREFERENCES_KEY_TASK_LIST, taskListJsonString)
        editor.apply()
    }

    override fun getList(presenter: MainPresenter): SortedMap<Int, Task>? {
        presenterView = presenter
        loaderManager.initLoader(SHARED_LOADER, null, this).forceLoad()
        //val loader = SharedLoader(context.getSharedPreferences(SHARED_PREFERENCES_FILE_TASKS, 0))
        //tasksList = loader.execute().get()

        //val sharedFile = context.getSharedPreferences(SHARED_PREFERENCES_FILE_TASKS, 0)
        //val savedTasksJsonString = sharedFile.getString(SHARED_PREFERENCES_KEY_TASK_LIST, "")
        //gson = GsonBuilder().setPrettyPrinting().create()
        //var tasksList = sortedMapOf<Int, Task>()
        //val itemType = object : TypeToken<SortedMap<Int, Task>>() {}.type
        //savedTasksJsonString?.let {
        //    if (!it.isEmpty()) {
        //        val taskListJSON = gson.fromJson<SortedMap<Int, Task>>(savedTasksJsonString, itemType)
        //        tasksList = taskListJSON
        //    }
        //}

        return taskMap
    }
}