package com.example.planner.asyncLoaders

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import com.example.planner.enums.TaskAction
import com.example.planner.storages.INTERNAL_ADD_TAG
import com.example.planner.storages.INTERNAL_EDIT_TAG
import com.example.planner.storages.INTERNAL_FILE_TASKS
import com.example.planner.storages.INTERNAL_REMOVE_TAG
import com.example.planner.task.Task
import com.google.gson.Gson
import java.io.OutputStreamWriter
import java.util.*

class InternalWriter(
    context: Context,
    private var task: Task?,
    private val action: TaskAction,
    private val tasks: SortedMap<Int, Task>
) : AsyncTaskLoader<Pair<SortedMap<Int, Task>, String>>(context) {

    override fun loadInBackground(): Pair<SortedMap<Int, Task>, String>? {
        val osw = OutputStreamWriter(context.openFileOutput(INTERNAL_FILE_TASKS, 0))

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
                Pair(tasks, INTERNAL_ADD_TAG)
            }
            TaskAction.ACTION_REMOVE -> {
                Pair(tasks, INTERNAL_REMOVE_TAG)
            }
            else -> {
                Pair(tasks, INTERNAL_EDIT_TAG)
            }
        }
    }
}