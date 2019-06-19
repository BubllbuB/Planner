package com.example.planner.asyncLoaders

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import com.example.planner.extensions.getTaskMap
import com.example.planner.storages.INTERNAL_FILE_TASKS
import com.example.planner.storages.INTERNAL_READ_TAG
import com.example.planner.task.Task
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.*

class InternalLoader(context: Context) : AsyncTaskLoader<Pair<SortedMap<Int, Task>, String>>(context) {
    private var tasksList = sortedMapOf<Int, Task>()

    override fun loadInBackground(): Pair<SortedMap<Int, Task>, String>? {
        val file = File(context.filesDir, INTERNAL_FILE_TASKS)

        if (file.exists() && file.length() > 0) {
            val buffStream = BufferedReader(InputStreamReader(context.openFileInput(INTERNAL_FILE_TASKS)))
            val tasksString = buffStream.readLine()
            tasksList = tasksString.getTaskMap()

            buffStream.close()
        }

        return Pair(tasksList, INTERNAL_READ_TAG)
    }
}