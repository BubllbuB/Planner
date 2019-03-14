package com.example.planner.asyncLoaders

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import com.example.planner.storages.SHARED_PREFERENCES_FILE_TASKS
import com.example.planner.storages.SHARED_PREFERENCES_KEY_TASK_LIST
import com.example.planner.task.Task
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.util.*

class SharedLoader(context: Context) : AsyncTaskLoader<SortedMap<Int, Task>>(context) {
    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE_TASKS, 0)
    private var tasksList = sortedMapOf<Int, Task>()
    private var gson = Gson()

    override fun loadInBackground(): SortedMap<Int, Task>? {
        val sharedFile = sharedPreferences
        val savedTasksJsonString = sharedFile.getString(SHARED_PREFERENCES_KEY_TASK_LIST, "")

        val editor = sharedFile.edit()
        editor.putString(SHARED_PREFERENCES_KEY_TASK_LIST, "")
        editor.apply()

        gson = GsonBuilder().setPrettyPrinting().create()
        val itemType = object : TypeToken<SortedMap<Int, Task>>() {}.type
        savedTasksJsonString?.let {
            if (!it.isEmpty()) {
                val taskListJSON = gson.fromJson<SortedMap<Int, Task>>(savedTasksJsonString, itemType)
                tasksList = taskListJSON
            }
        }
        return tasksList
    }
}