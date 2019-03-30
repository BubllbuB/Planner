package com.example.planner.adapter

import android.support.v7.util.DiffUtil
import com.example.planner.task.Task

class DiffTaskCallback: DiffUtil.ItemCallback<Task>() {

    override fun areContentsTheSame(oldTask: Task, newTask: Task): Boolean {
        val compareTitle = oldTask.title == newTask.title
        val compareDescription = oldTask.description == newTask.description
        val compareDone = oldTask.done == newTask.done
        val compareFavorite = oldTask.favorite == newTask.favorite

        return compareTitle && compareDescription && compareDone && compareFavorite
    }

    override fun areItemsTheSame(oldTask: Task, newTask: Task): Boolean {
        return oldTask.id == newTask.id
    }
}