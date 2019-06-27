package com.example.planner.observer

import com.example.planner.task.Task

interface StorageObserver {
    fun onUpdateMap(map: Map<Int,Task>)
    fun reloadStorage()
}