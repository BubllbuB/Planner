package com.example.planner.asyncLoaders

import android.content.Context
import android.database.Cursor
import android.support.v4.content.AsyncTaskLoader
import com.example.planner.sqlhelper.DB_TABLE_NAME
import com.example.planner.sqlhelper.StorageSQLiteOpenHelper
import com.example.planner.task.Task
import java.util.*


class DatabaseLoader(context: Context) : AsyncTaskLoader<SortedMap<Int, Task>>(context) {
    private var tasksList = sortedMapOf<Int, Task>()
    private val database = StorageSQLiteOpenHelper(context, DB_TABLE_NAME, null, 1)

    override fun loadInBackground(): SortedMap<Int, Task>? {
        val db = database.readableDatabase
        val cursor = db.rawQuery("select * from $DB_TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            do {
                for (i in 0 until cursor.columnCount) {
                    val task = parseTask(cursor)
                    tasksList[task.id] = task
                }


            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return tasksList
    }

    private fun parseTask(cursor: Cursor): Task {
        val id = cursor.getInt(0)
        val title = cursor.getString(1)
        val description = cursor.getString(2)
        val fav = cursor.getInt(3) == 1
        val done = cursor.getInt(4) == 1

        return Task(title, description, id, fav, done)
    }
}