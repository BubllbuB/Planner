package com.example.planner.asyncLoaders

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import com.example.planner.storages.SHARED_PREFERENCES_FILE_TASKS
import com.example.planner.task.Task
import com.google.gson.GsonBuilder
import java.util.*

class SharedLoader(context: Context) : AsyncTaskLoader<SortedMap<Int, Task>>(context) {
    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE_TASKS, 0)
    private var tasksList = sortedMapOf<Int, Task>()

    override fun loadInBackground(): SortedMap<Int, Task>? {
        val sharedFile = sharedPreferences

        val strings = sharedFile.all
        strings.remove("lastId")

        val gson = GsonBuilder().setPrettyPrinting().create()
        for ((_, value) in strings) {
            val taskJ = gson.fromJson<Task>(value.toString(), Task::class.java)
            tasksList[taskJ.id] = taskJ
        }

        return tasksList
    }
}