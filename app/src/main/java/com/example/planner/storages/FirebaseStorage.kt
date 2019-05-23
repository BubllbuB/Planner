package com.example.planner.storages

import com.example.planner.observer.StorageObserver
import com.example.planner.task.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*


internal object FirebaseStorage : Storage {
    private var taskMap = sortedMapOf<Int, Task>()
    private val observers: MutableList<StorageObserver> = ArrayList()

    private val database = FirebaseDatabase.getInstance()
    private val dbReference = database.reference.child("tasks")

    fun init(): FirebaseStorage {
        return this
    }

    override fun addTask(task: Task) {
        var lastId = -1

        val lastQuery = dbReference.orderByKey().limitToLast(1)
        lastQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (child in dataSnapshot.children) {
                    lastId = child.getValue(Task::class.java)?.id ?: -1
                }

                task.id = ++lastId

                dbReference.child(task.id.toString()).setValue(
                    task
                ) { _, _ ->
                    taskMap[task.id] = task
                    notifyObservers(taskMap)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun removeTask(task: Task) {
        dbReference.child(task.id.toString())
            .removeValue { _, _ ->
                taskMap.remove(task.id)
                notifyObservers(taskMap)
            }
    }

    override fun editTask(task: Task) {
        val updateMap = mutableMapOf<String, Any?>()
        updateMap["title"] = task.title
        updateMap["description"] = task.description
        updateMap["favorite"] = task.favorite
        updateMap["done"] = task.done

        dbReference.child(task.id.toString())
            .updateChildren(updateMap) { _, _ ->
                taskMap[task.id] = task
                notifyObservers(taskMap)
            }
    }

    override fun getList() {
        if (taskMap.isEmpty()) {
            dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (postSnapshot in dataSnapshot.children) {
                        val task = postSnapshot.getValue(Task::class.java)
                        taskMap[task?.id] = task
                    }
                    notifyObservers(taskMap)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    throw databaseError.toException()
                }
            })
        } else {
            notifyObservers(taskMap)
        }
    }

    override fun addObserver(observer: StorageObserver) {
        observers.add(observer)
    }

    override fun removeObserver(observer: StorageObserver) {
        observers.remove(observer)
    }

    private fun notifyObservers(tasks: Map<Int, Task>) {
        observers.forEach { it.onUpdateMap(tasks) }
    }
}