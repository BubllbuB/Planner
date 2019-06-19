package com.example.planner.asyncLoaders

import android.content.Context
import android.os.Environment
import android.support.v4.content.AsyncTaskLoader
import com.example.planner.enums.TaskAction
import com.example.planner.storages.EXTERNAL_ADD_TAG
import com.example.planner.storages.EXTERNAL_EDIT_TAG
import com.example.planner.storages.EXTERNAL_FILE_TASKS
import com.example.planner.storages.EXTERNAL_REMOVE_TAG
import com.example.planner.task.Task
import com.google.gson.Gson
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.util.*

class ExternalWriter(
    context: Context,
    private var task: Task?,
    private val action: TaskAction,
    private val tasks: SortedMap<Int, Task>
) : AsyncTaskLoader<Pair<SortedMap<Int, Task>, String>>(context) {

    override fun loadInBackground(): Pair<SortedMap<Int, Task>, String>? {
        val file = File(
            Environment.getExternalStorageDirectory(), EXTERNAL_FILE_TASKS
        )

        val osw = OutputStreamWriter(FileOutputStream(file))
        val lastId = if (tasks.isEmpty()) 0 else tasks.lastKey()

        when (action) {
            TaskAction.ACTION_ADD -> {
                task?.id = lastId + 1
                tasks[task?.id] = task
            }
            TaskAction.ACTION_REMOVE -> {
                tasks.remove(task?.id)
            }
            else -> {
                tasks[task?.id] = task
            }
        }

        val taskListString = Gson().toJson(tasks)

        osw.write(taskListString)
        osw.flush()
        osw.close()

        return when (action) {
            TaskAction.ACTION_ADD -> {
                Pair(tasks, EXTERNAL_ADD_TAG)
            }
            TaskAction.ACTION_REMOVE -> {
                Pair(tasks, EXTERNAL_REMOVE_TAG)
            }
            else -> {
                Pair(tasks, EXTERNAL_EDIT_TAG)
            }
        }
    }
}