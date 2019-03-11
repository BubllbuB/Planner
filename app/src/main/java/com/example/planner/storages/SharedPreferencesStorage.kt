package com.example.planner.storages

import android.content.Context
import com.example.planner.task.Task
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.util.*


const val SHARED_PREFERENCES_FILE_TASKS = "tasksList"
const val SHARED_PREFERENCES_KEY_TASK_LIST = "tasks"
const val SHARED_PREFERENCES_KEY_LAST_ID = "lastId"

class SharedPreferencesStorage(private val context: Context, private val tasksList: SortedMap<Int, Task>) : Storage {
    private var gson = Gson()
    var taskListJsonString = ""

    override fun addTask(task: Task?) {
        val sharedFile = context.getSharedPreferences(SHARED_PREFERENCES_FILE_TASKS, 0)
        val editor = sharedFile.edit()

        val lastId = sharedFile.getInt(SHARED_PREFERENCES_KEY_LAST_ID, 0)

        task?.id = lastId + 1
        tasksList[task?.id] = task

        for(a in 1 .. 90000) {
            task?.id = lastId + a
            task?.title = (lastId + a).toString()
            tasksList[task?.id] = task
        }

        taskListJsonString = gson.toJson(tasksList)

        var savedTasksJsonString = sharedFile.getString(SHARED_PREFERENCES_KEY_TASK_LIST, "")
        if (!savedTasksJsonString.isNullOrEmpty()) {
            taskListJsonString = taskListJsonString.removeRange(0, 1)
            savedTasksJsonString = savedTasksJsonString.dropLast(1)
            editor.putString(SHARED_PREFERENCES_KEY_TASK_LIST, "$savedTasksJsonString,$taskListJsonString")
        } else
            editor.putString(SHARED_PREFERENCES_KEY_TASK_LIST, taskListJsonString)

        editor.putInt(SHARED_PREFERENCES_KEY_LAST_ID, lastId + 1)
        editor.apply()
    }

    override fun removeTask(task: Task?) {
        val sharedFile = context.getSharedPreferences(SHARED_PREFERENCES_FILE_TASKS, 0)
        val editor = sharedFile.edit()

        //val loader = SharedLoader(context.getSharedPreferences(SHARED_PREFERENCES_FILE_TASKS, 0))
        //tasksList = loader.execute().get()

        tasksList.remove(task?.id)

        taskListJsonString = gson.toJson(tasksList)
        editor.putString(SHARED_PREFERENCES_KEY_TASK_LIST, taskListJsonString)
        editor.apply()
    }

    override fun editTask(task: Task?) {
        val sharedFile = context.getSharedPreferences(SHARED_PREFERENCES_FILE_TASKS, 0)
        val editor = sharedFile.edit()

        //val loader = SharedLoader(context.getSharedPreferences(SHARED_PREFERENCES_FILE_TASKS, 0))
        //tasksList = loader.execute().get()

        tasksList[task?.id] = task

        taskListJsonString = gson.toJson(tasksList)
        editor.putString(SHARED_PREFERENCES_KEY_TASK_LIST, taskListJsonString)
        editor.apply()
    }

    override fun getList(): SortedMap<Int, Task>? {
        //val loader = SharedLoader(context.getSharedPreferences(SHARED_PREFERENCES_FILE_TASKS, 0))
        //tasksList = loader.execute().get()

        //val sharedFile = context.getSharedPreferences(SHARED_PREFERENCES_FILE_TASKS, 0)
        //val savedTasksJsonString = sharedFile.getString(SHARED_PREFERENCES_KEY_TASK_LIST, "")
        //gson = GsonBuilder().setPrettyPrinting().create()
        //var tasksList = sortedMapOf<Int, Task>()
        //val itemType = object : TypeToken<SortedMap<Int, Task>>() {}.type
        //savedTasksJsonString?.let {
        //    if (!it.isEmpty()) {
        //        val taskListJSON = gson.fromJson<SortedMap<Int, Task>>(savedTasksJsonString, itemType)
        //        tasksList = taskListJSON
        //    }
        //}

        return tasksList
    }
}