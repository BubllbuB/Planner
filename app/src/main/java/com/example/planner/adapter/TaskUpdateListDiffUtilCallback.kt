package com.example.planner.adapter

import android.support.v7.util.DiffUtil
import com.example.planner.task.Task

class TaskUpdateListDiffUtilCallback(private val oldList: List<Task>, private val newList: List<Task>) :
    DiffUtil.Callback() {

    private var posHeadOther = 0

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
                val compareTitle = oldList[oldItemPosition - offsetOld].title == newList[newItemPosition - offsetNew].title
                val compareDescription =
                    oldList[oldItemPosition - offsetOld].description == newList[newItemPosition - offsetNew].description
                val compareDone = oldList[oldItemPosition - offsetOld].done == newList[newItemPosition - offsetNew].done
                val compareFavorite =
                    oldList[oldItemPosition - offsetOld].favorite == newList[newItemPosition - offsetNew].favorite

                val returnedV = compareTitle && compareDescription && compareDone && compareFavorite

                return compareTitle && compareDescription && compareDone && compareFavorite
            } else {
                false
            }
        }

        return if (oldPosHeadOther != oldItemPosition && newPosHeadOther != newItemPosition) {
            val compareTitle = oldList[oldItemPosition - offsetOld].title == newList[newItemPosition - offsetNew].title
            val compareDescription =
                oldList[oldItemPosition - offsetOld].description == newList[newItemPosition - offsetNew].description
            val compareDone = oldList[oldItemPosition - offsetOld].done == newList[newItemPosition - offsetNew].done
            val compareFavorite =
                oldList[oldItemPosition - offsetOld].favorite == newList[newItemPosition - offsetNew].favorite

            val returnedV = compareTitle && compareDescription && compareDone && compareFavorite

            return compareTitle && compareDescription && compareDone && compareFavorite
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
            } else {
                false
            }
        }

        return if (oldPosHeadOther != oldItemPosition && newPosHeadOther != newItemPosition) {
            oldList[oldItemPosition - offsetOld].id == newList[newItemPosition - offsetNew].id
        } else oldPosHeadOther == oldItemPosition && newPosHeadOther == newItemPosition
    }

    override fun getNewListSize(): Int {
        posHeadOther = newList.indexOfFirst { !it.favorite } + 1

        return if (newList.isNotEmpty()) {
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
        posHeadOther = oldList.indexOfFirst { !it.favorite } + 1

        return if (oldList.isNotEmpty()) {
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