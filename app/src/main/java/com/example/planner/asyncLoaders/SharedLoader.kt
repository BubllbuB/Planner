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

        //val editor = sharedFile.edit()
        //editor.clear()
        //editor.apply()

        //val savedTasksJsonString = sharedFile.getString(SHARED_PREFERENCES_KEY_TASK_LIST, "")

        val strings = sharedFile.all
        strings.remove("lastId")
        gson = GsonBuilder().setPrettyPrinting().create()
        val itemType = object : TypeToken<Task>() {}.type
        for ((_, value) in strings) {
            val taskJ = gson.fromJson<Task>(value.toString(),itemType)
            tasksList[taskJ.id] = taskJ
        }

        return tasksList
    }
}