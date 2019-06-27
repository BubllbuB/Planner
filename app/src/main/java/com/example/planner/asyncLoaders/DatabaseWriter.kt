package com.example.planner.asyncLoaders

import android.content.ContentValues
import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import com.example.planner.enums.TaskAction
import com.example.planner.sqlhelper.*
import com.example.planner.storages.DATABASE_ADD_TAG
import com.example.planner.storages.DATABASE_EDIT_TAG
import com.example.planner.storages.DATABASE_REMOVE_TAG
import com.example.planner.task.Task
import java.util.*


class DatabaseWriter(
    context: Context,
    private var task: Task?,
    private val action: TaskAction,
    private val tasks: SortedMap<Int, Task>
) : AsyncTaskLoader<Pair<SortedMap<Int, Task>, String>>(context) {
    private val database = StorageSQLiteOpenHelper(context, DB_TABLE_NAME, null, 1)

    override fun loadInBackground(): Pair<SortedMap<Int, Task>, String>? {
        val db = database.writableDatabase
        val taskValues = ContentValues()

        task?.let {
            taskValues.put(DB_TASK_TITLE, it.title)
            taskValues.put(DB_TASK_DESCRIPTION, it.description)
            taskValues.put(DB_TASK_FAVORITE, if (it.favorite) 1 else 0)
            taskValues.put(DB_TASK_DONE, if (it.done) 1 else 0)
        }

        val lastId = getLastId()

        when (action) {
            TaskAction.ACTION_ADD -> {
                task?.id = lastId + 1
                tasks[task?.id] = task
                db.insert(DB_TABLE_NAME, null, taskValues)

            }
            TaskAction.ACTION_REMOVE -> {
                db.delete(DB_TABLE_NAME, DB_TASK_ID + "=" + task?.id, null)
                tasks.remove(task?.id)
            }
            else -> {
                db.update(DB_TABLE_NAME, taskValues, DB_TASK_ID + "=" + task?.id, null)
                tasks[task?.id] = task
            }
        }

        db.close()

        return when (action) {
            TaskAction.ACTION_ADD -> {
                Pair(tasks, DATABASE_ADD_TAG)
            }
            TaskAction.ACTION_REMOVE -> {
                Pair(tasks, DATABASE_REMOVE_TAG)
            }
            else -> {
                Pair(tasks, DATABASE_EDIT_TAG)
            }
        }
    }

    private fun getLastId(): Int {
        val db = database.readableDatabase
        val cursor = db.rawQuery("select max($DB_TASK_ID) from $DB_TABLE_NAME", null)

        var id = 0
        if (cursor.moveToFirst()) id = cursor.getInt(0)

        cursor.close()
        return id
    }
}