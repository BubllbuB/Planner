package com.example.planner.asyncLoaders

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import com.example.planner.extensions.getTask
import com.example.planner.storages.SHARED_PREFERENCES_FILE_TASKS
import com.example.planner.storages.SHARED_READ_TAG
import com.example.planner.task.Task
import java.util.*

class SharedLoader(context: Context) : AsyncTaskLoader<Pair<SortedMap<Int, Task>, String>>(context) {
    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE_TASKS, 0)
    private var tasksList = sortedMapOf<Int, Task>()

    override fun loadInBackground(): Pair<SortedMap<Int, Task>, String>? {
        val strings = sharedPreferences.all
        strings.remove("lastId")

        for ((_, value) in strings) {
            val taskJ = value.toString().getTask()
            tasksList[taskJ.id] = taskJ
        }

        return Pair(tasksList, SHARED_READ_TAG)
    }
}