package com.example.planner.asyncLoaders

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import com.example.planner.enums.TaskActionId
import com.example.planner.storages.INTERNAL_FILE_TASKS
import com.example.planner.task.Task
import com.google.gson.Gson
import java.io.OutputStreamWriter
import java.util.*

class InternalWriter(
    context: Context,
    private var task: Task?,
    private val action: Int?,
    private val tasks: SortedMap<Int, Task>
) : AsyncTaskLoader<SortedMap<Int, Task>>(context) {

    override fun loadInBackground(): SortedMap<Int, Task>? {
        val osw = OutputStreamWriter(context.openFileOutput(INTERNAL_FILE_TASKS, 0))

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