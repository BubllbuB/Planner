package com.example.planner.asyncLoaders

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import com.example.planner.extensions.parseTask
import com.example.planner.sqlhelper.DB_TABLE_NAME
import com.example.planner.sqlhelper.StorageSQLiteOpenHelper
import com.example.planner.storages.DATABASE_READ_TAG
import com.example.planner.task.Task
import java.util.*


class DatabaseLoader(context: Context) : AsyncTaskLoader<Pair<SortedMap<Int, Task>, String>>(context) {
    private var tasksList = sortedMapOf<Int, Task>()
    private val database = StorageSQLiteOpenHelper(context, DB_TABLE_NAME, null, 1)

    override fun loadInBackground(): Pair<SortedMap<Int, Task>,String>? {
        val db = database.readableDatabase
        val cursor = db.rawQuery("select * from $DB_TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            do {
                for (i in 0 until cursor.columnCount) {
                    val task = cursor.parseTask()
                    tasksList[task.id] = task
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return Pair(tasksList, DATABASE_READ_TAG)
    }
}