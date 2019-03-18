package com.example.planner.observer

import com.example.planner.task.Task

interface StorageObserver {
    fun onUpdateList(list: ArrayList<Task>)
}