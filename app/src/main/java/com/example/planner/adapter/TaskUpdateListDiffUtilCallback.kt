package com.example.planner.adapter

import android.support.v7.util.DiffUtil
import com.example.planner.task.Task

class TaskUpdateListDiffUtilCallback(private val oldList: List<Task>, private val newList: List<Task>) :
    DiffUtil.Callback() {

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldPosHeadFav = if (oldList[0].favorite) 0 else -1
        val oldPosHeadOther = if (oldList[0].favorite) oldList.indexOfFirst { !it.favorite } + 1 else -1
        var offsetOld = if (oldPosHeadOther > 0 && oldList[0].favorite) 1 else 0
        if (oldPosHeadOther in 1..oldItemPosition && oldList[0].favorite) offsetOld = 2


        val newPosHeadFav = if (newList[0].favorite) 0 else -1
        val newPosHeadOther = if (newList[0].favorite) newList.indexOfFirst { !it.favorite } + 1 else -1
        var offsetNew = if (newPosHeadOther > 0 && newList[0].favorite) 1 else 0
        if (newPosHeadOther in 1..newItemPosition && newList[0].favorite) offsetNew = 2


        if (oldItemPosition == 0 || newItemPosition == 0) {
            return if (oldPosHeadFav == 0 && newPosHeadFav == 0) {
                true
            } else if (oldPosHeadFav == -1 && newPosHeadFav == -1) {
                return oldList[oldItemPosition - offsetOld] == newList[newItemPosition - offsetNew]
            } else if (oldPosHeadFav == 0 && newPosHeadFav == -1 && newItemPosition==0 && oldItemPosition!=0 && oldItemPosition!=oldPosHeadOther){
                return oldList[oldItemPosition - offsetOld] == newList[newItemPosition - offsetNew]
            } else if (oldPosHeadFav == -1 && newPosHeadFav == 0 && oldItemPosition==0 && newItemPosition!=0 && newItemPosition!=newPosHeadOther){
                return oldList[oldItemPosition - offsetOld] == newList[newItemPosition - offsetNew]
            } else {
                false
            }
        }

        return if (oldPosHeadOther != oldItemPosition && newPosHeadOther != newItemPosition) {
            return oldList[oldItemPosition - offsetOld] == newList[newItemPosition - offsetNew]
        } else oldPosHeadOther == oldItemPosition && newPosHeadOther == newItemPosition


    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldPosHeadFav = if (oldList[0].favorite) 0 else -1
        val oldPosHeadOther = if (oldList[0].favorite) oldList.indexOfFirst { !it.favorite } + 1 else -1
        var offsetOld = if (oldPosHeadOther > 0 && oldList[0].favorite) 1 else 0
        if (oldPosHeadOther in 1..oldItemPosition && oldList[0].favorite) offsetOld = 2


        val newPosHeadFav = if (newList[0].favorite) 0 else -1
        val newPosHeadOther = if (newList[0].favorite) newList.indexOfFirst { !it.favorite } + 1 else -1
        var offsetNew = if (newPosHeadOther > 0 && newList[0].favorite) 1 else 0
        if (newPosHeadOther in 1..newItemPosition && newList[0].favorite) offsetNew = 2


        if (oldItemPosition == 0 || newItemPosition == 0) {
            return if (oldPosHeadFav == 0 && newPosHeadFav == 0) {
                true
            } else if (oldPosHeadFav == -1 && newPosHeadFav == -1) {
                oldList[oldItemPosition - offsetOld].id == newList[newItemPosition - offsetNew].id
            } else if (oldPosHeadFav == 0 && newPosHeadFav == -1 && newItemPosition==0 && oldItemPosition!=0 && oldItemPosition!=oldPosHeadOther){
                oldList[oldItemPosition - offsetOld].id == newList[newItemPosition - offsetNew].id
            } else if (oldPosHeadFav == -1 && newPosHeadFav == 0 && oldItemPosition==0 && newItemPosition!=0 && newItemPosition!=newPosHeadOther){
                oldList[oldItemPosition - offsetOld].id == newList[newItemPosition - offsetNew].id
            } else {
                false
            }
        }

        return if (oldPosHeadOther != oldItemPosition && newPosHeadOther != newItemPosition) {
            oldList[oldItemPosition - offsetOld].id == newList[newItemPosition - offsetNew].id
        } else oldPosHeadOther == oldItemPosition && newPosHeadOther == newItemPosition
    }

    override fun getNewListSize(): Int {
        return if (newList.isNotEmpty()) {
            val posHeadOther = newList.indexOfFirst { !it.favorite } + 1

            if (newList[0].favorite && posHeadOther > 0) {
                newList.size + 2
            } else {
                newList.size
            }
        } else {
            newList.size
        }
    }

    override fun getOldListSize(): Int {
        return if (oldList.isNotEmpty()) {
            val posHeadOther = if (oldList[0].favorite) oldList.indexOfFirst { !it.favorite } + 1 else -1

            if (oldList[0].favorite && posHeadOther > 0) {
                oldList.size + 2
            } else {
                oldList.size
            }
        } else {
            oldList.size
        }
    }
}