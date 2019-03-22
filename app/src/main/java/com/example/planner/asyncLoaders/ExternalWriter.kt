package com.example.planner.asyncLoaders

import android.content.Context
import android.os.Environment
import android.support.v4.content.AsyncTaskLoader
import com.example.planner.enums.TaskActionId
import com.example.planner.storages.EXTERNAL_FILE_TASKS
import com.example.planner.task.Task
import com.google.gson.Gson
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.util.*

class ExternalWriter(
    context: Context,
    private var task: Task?,
    private val action: Int?,
    private val tasks: SortedMap<Int, Task>
) : AsyncTaskLoader<SortedMap<Int, Task>>(context) {

    override fun loadInBackground(): SortedMap<Int, Task>? {
        val file = File(
            Environment.getExternalStorageDirectory(), EXTERNAL_FILE_TASKS
        )

        val osw = OutputStreamWriter(FileOutputStream(file))
        val lastId = if (tasks.isEmpty()) 0 else tasks.lastKey()

        when (action) {
            TaskActionId.ACTION_ADD.getId() -> {
                task?.id = lastId + 1
                tasks[task?.id] = task
            }
            TaskActionId.ACTION_REMOVE.getId() -> {
                tasks.remove(task?.id)
            }
            TaskActionId.ACTION_EDIT.getId() -> {
                tasks[task?.id] = task
            }
        }

        val taskListString = Gson().toJson(tasks)

        osw.write(taskListString)
        osw.flush()
        osw.close()

        return tasks
    }
}