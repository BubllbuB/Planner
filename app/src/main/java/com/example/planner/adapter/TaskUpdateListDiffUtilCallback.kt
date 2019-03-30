package com.example.planner.adapter

import android.support.v7.util.DiffUtil
import com.example.planner.task.Task

class TaskUpdateListDiffUtilCallback(private val oldList: List<Task>, private val newList: List<Task>) :
    DiffUtil.Callback() {

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val compareTitle = oldList[oldItemPosition].title == newList[newItemPosition].title
        val compareDescription = oldList[oldItemPosition].description == newList[newItemPosition].description
        val compareDone = oldList[oldItemPosition].done == newList[newItemPosition].done
        val compareFavorite = oldList[oldItemPosition].favorite == newList[newItemPosition].favorite

        return compareTitle && compareDescription && compareDone && compareFavorite
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }
}