package com.example.planner.asyncLoaders

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import com.example.planner.storages.*
import com.example.planner.task.Task
import com.google.gson.Gson
import java.util.*

class SharedWriter(
    context: Context,
    private var task: Task?,
    private val action: Int?,
    private val tasks: SortedMap<Int, Task>
) : AsyncTaskLoader<SortedMap<Int, Task>>(context) {
    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE_TASKS, 0)
    private var gson = Gson()

    override fun onStartLoading() {
        super.onStartLoading()
        forceLoad()
    }

    override fun loadInBackground(): SortedMap<Int, Task>? {
        val sharedFile = sharedPreferences
        val editor = sharedFile.edit()
        val lastId = sharedFile.getInt(SHARED_PREFERENCES_KEY_LAST_ID, 0)

        when (action) {
            WRITER_ADD -> {
                task?.id = lastId + 1
                tasks[task?.id] = task
                val taskJson = gson.toJson(task)
                editor.putString(SHARED_PREFERENCES_KEY_TASK + task?.id, taskJson)
                editor.putInt(SHARED_PREFERENCES_KEY_LAST_ID, lastId + 1)
            }
            WRITER_REMOVE -> {
                tasks.remove(task?.id)
                editor.remove(SHARED_PREFERENCES_KEY_TASK + task?.id)
            }
            WRITER_EDIT -> {
                tasks[task?.id] = task
                val taskJson = gson.toJson(task)
                editor.putString(SHARED_PREFERENCES_KEY_TASK + task?.id, taskJson)
            }
        }

        editor.apply()
        return tasks
    }
}