package com.example.planner.asyncLoaders

import android.content.Context
import android.os.Environment
import android.support.v4.content.AsyncTaskLoader
import com.example.planner.storages.EXTERNAL_FILE_TASKS
import com.example.planner.storages.EXTERNAL_WRITER_ADD
import com.example.planner.storages.EXTERNAL_WRITER_EDIT
import com.example.planner.storages.EXTERNAL_WRITER_REMOVE
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
    private var gson = Gson()

    override fun loadInBackground(): SortedMap<Int, Task>? {
        val file = File(
            Environment.getExternalStorageDirectory(), EXTERNAL_FILE_TASKS
        )

        val osw = OutputStreamWriter(FileOutputStream(file))

        val lastId = if (!tasks.isEmpty()) tasks.lastKey() else 0

        when (action) {
            EXTERNAL_WRITER_ADD -> {
                task?.id = lastId + 1
                tasks[task?.id] = task
            }
            EXTERNAL_WRITER_REMOVE -> {
                tasks.remove(task?.id)
            }
            EXTERNAL_WRITER_EDIT -> {
                tasks[task?.id] = task
            }
        }

        val taskListString = gson.toJson(tasks)

        osw.write(taskListString)
        osw.close()

        return tasks
    }
}