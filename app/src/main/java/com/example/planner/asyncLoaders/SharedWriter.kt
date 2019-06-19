package com.example.planner.asyncLoaders

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import com.example.planner.enums.TaskAction
import com.example.planner.storages.*
import com.example.planner.task.Task
import com.google.gson.Gson
import java.util.*

class SharedWriter(
    context: Context,
    private var task: Task?,
    private val action: TaskAction,
    private val tasks: SortedMap<Int, Task>
) : AsyncTaskLoader<Pair<SortedMap<Int, Task>, String>>(context) {
    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE_TASKS, 0)

    override fun loadInBackground(): Pair<SortedMap<Int, Task>, String>? {
        val editor = sharedPreferences.edit()
        val lastId = sharedPreferences.getInt(SHARED_PREFERENCES_KEY_LAST_ID, 0)

        when (action) {
            TaskAction.ACTION_ADD -> {
                task?.id = lastId + 1
                tasks[task?.id] = task
                val taskJson = Gson().toJson(task)
                editor.putString(SHARED_PREFERENCES_KEY_TASK + task?.id, taskJson)
                editor.putInt(SHARED_PREFERENCES_KEY_LAST_ID, lastId + 1)
            }
            TaskAction.ACTION_REMOVE -> {
                tasks.remove(task?.id)
                editor.remove(SHARED_PREFERENCES_KEY_TASK + task?.id)
            }
            else -> {
                tasks[task?.id] = task
                val taskJson = Gson().toJson(task)
                editor.putString(SHARED_PREFERENCES_KEY_TASK + task?.id, taskJson)
            }
        }

        editor.apply()

        return when (action) {
            TaskAction.ACTION_ADD -> {
                Pair(tasks, SHARED_ADD_TAG)
            }
            TaskAction.ACTION_REMOVE -> {
                Pair(tasks, SHARED_REMOVE_TAG)
            }
            else -> {
                Pair(tasks, SHARED_EDIT_TAG)
            }
        }
    }
}