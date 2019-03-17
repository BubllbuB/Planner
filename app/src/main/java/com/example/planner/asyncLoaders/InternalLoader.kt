package com.example.planner.asyncLoaders

import android.content.Context
import android.os.Environment
import android.support.v4.content.AsyncTaskLoader
import com.example.planner.storages.INTERNAL_FILE_TASKS
import com.example.planner.task.Task
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*
import java.util.*

class InternalLoader(context: Context) : AsyncTaskLoader<SortedMap<Int, Task>>(context) {
    private var tasksList = sortedMapOf<Int, Task>()
    private var gson = Gson()

    override fun loadInBackground(): SortedMap<Int, Task>? {
        val file = File(
            context.getExternalFilesDir(
                Environment.DIRECTORY_DOCUMENTS
            ), INTERNAL_FILE_TASKS
        )

        if(file.exists() && file.length() > 0) {
            val buffStream = BufferedReader(InputStreamReader(FileInputStream(file)))
            val tasksString = buffStream.readLine ()
            val itemType = object : TypeToken<SortedMap<Int, Task>>() {}.type
            tasksList = gson.fromJson<SortedMap<Int, Task>>(tasksString,itemType)

            buffStream.close()
        }

        return tasksList
    }
}