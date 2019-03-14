package com.example.planner.asyncLoaders

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import com.example.planner.storages.SHARED_PREFERENCES_FILE_TASKS
import com.example.planner.storages.SHARED_PREFERENCES_KEY_LAST_ID
import com.example.planner.storages.SHARED_PREFERENCES_KEY_TASK_LIST
import com.example.planner.task.Task
import com.google.gson.Gson
import java.util.*

class SharedWriter(context: Context, private val gsonString: String?) : AsyncTaskLoader<SortedMap<Int, Task>>(context) {
    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE_TASKS, 0)
    private var tasksList = sortedMapOf<Int, Task>()
    private var gson = Gson()

    override fun loadInBackground(): SortedMap<Int, Task>? {
        val sharedFile = sharedPreferences
        val editor = sharedFile.edit()
        var taskListJsonString = gsonString
        val lastId = sharedFile.getInt(SHARED_PREFERENCES_KEY_LAST_ID, 0)


        for (a in 1..90000) {
            val t = Task(a.toString(),a.toString())
            t.id = lastId + a
            t.title = (lastId + a).toString()
            tasksList[t.id] = t
        }

        taskListJsonString = gson.toJson(tasksList)

        var savedTasksJsonString = sharedFile.getString(SHARED_PREFERENCES_KEY_TASK_LIST, "")
        if (!savedTasksJsonString.isNullOrEmpty()) {
            taskListJsonString = taskListJsonString?.removeRange(0, 1)
            savedTasksJsonString = savedTasksJsonString.dropLast(1)
            editor.putString(SHARED_PREFERENCES_KEY_TASK_LIST, "$savedTasksJsonString,$taskListJsonString")
        } else
            editor.putString(SHARED_PREFERENCES_KEY_TASK_LIST, taskListJsonString)

        editor.putInt(SHARED_PREFERENCES_KEY_LAST_ID, lastId + 1)
        editor.apply()


        return tasksList
    }
}