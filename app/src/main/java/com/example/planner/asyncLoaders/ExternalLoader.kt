package com.example.planner.asyncLoaders

import android.content.Context
import android.os.Environment
import android.support.v4.content.AsyncTaskLoader
import com.example.planner.extensions.getTaskMap
import com.example.planner.storages.EXTERNAL_FILE_TASKS
import com.example.planner.storages.EXTERNAL_READ_TAG
import com.example.planner.task.Task
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.*

class ExternalLoader(context: Context) : AsyncTaskLoader<Pair<SortedMap<Int, Task>, String>>(context) {
    private var tasksList = sortedMapOf<Int, Task>()

    override fun loadInBackground(): Pair<SortedMap<Int, Task>, String>? {
        val file = File(
            Environment.getExternalStorageDirectory(), EXTERNAL_FILE_TASKS
        )

        if (file.exists() && file.length() > 0) {
            val buffStream = BufferedReader(InputStreamReader(FileInputStream(file)))
            val tasksString = buffStream.readLine()
            tasksList = tasksString.getTaskMap()

            buffStream.close()
        }

        return Pair(tasksList, EXTERNAL_READ_TAG)
    }
}