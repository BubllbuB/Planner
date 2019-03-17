package com.example.planner.sqlhelper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

const val DB_TABLE_NAME = "tasks"
const val DB_TASK_ID = "id"
const val DB_TASK_TITLE = "title"
const val DB_TASK_DESCRIPTION = "description"
const val DB_TASK_FAVORITE = "favorite"
const val DB_TASK_DONE = "done"

class StorageSQLiteOpenHelper(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table $DB_TABLE_NAME ($DB_TASK_ID INTEGER PRIMARY KEY AUTOINCREMENT, $DB_TASK_TITLE TEXT, $DB_TASK_DESCRIPTION TEXT NOT NULL, $DB_TASK_FAVORITE INTEGER DEFAULT 0, $DB_TASK_DONE INTEGER DEFAULT 0)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}