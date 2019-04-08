package com.example.planner.adapter

import android.support.v7.util.DiffUtil
import com.example.planner.extensions.getOffset
import com.example.planner.task.Task

class TaskUpdateListDiffUtilCallback(private val oldList: List<Task>, private val newList: List<Task>) :
    DiffUtil.Callback() {
    private var oldPosHeadFav = -1
    private var oldPosHeadOther = -1
    private var offsetOld = 0
    private var newPosHeadFav = -1
    private var newPosHeadOther = -1
    private var offsetNew = 0


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        recalculatePositions(oldItemPosition, newItemPosition)

        if (oldItemPosition == 0 || newItemPosition == 0) {
            return if (oldPosHeadFav == 0 && newPosHeadFav == 0) {
                true
            } else if (oldPosHeadFav == -1 && newPosHeadFav == -1) {
                oldList[oldItemPosition - offsetOld] == newList[newItemPosition - offsetNew]
            } else if (oldPosHeadFav == 0 && newPosHeadFav == -1 && newItemPosition == 0 && oldItemPosition != 0 && oldItemPosition != oldPosHeadOther) {
                oldList[oldItemPosition - offsetOld] == newList[newItemPosition - offsetNew]
            } else if (oldPosHeadFav == -1 && newPosHeadFav == 0 && oldItemPosition == 0 && newItemPosition != 0 && newItemPosition != newPosHeadOther) {
                oldList[oldItemPosition - offsetOld] == newList[newItemPosition - offsetNew]
            } else {
                false
            }
        }

        return if (oldPosHeadOther != oldItemPosition && newPosHeadOther != newItemPosition) {
            oldList[oldItemPosition - offsetOld] == newList[newItemPosition - offsetNew]
        } else oldPosHeadOther == oldItemPosition && newPosHeadOther == newItemPosition
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        recalculatePositions(oldItemPosition, newItemPosition)

        if (oldItemPosition == 0 || newItemPosition == 0) {
            return if (oldPosHeadFav == 0 && newPosHeadFav == 0) {
                true
            } else if (oldPosHeadFav == -1 && newPosHeadFav == -1) {
                oldList[oldItemPosition - offsetOld].id == newList[newItemPosition - offsetNew].id
            } else if (oldPosHeadFav == 0 && newPosHeadFav == -1 && newItemPosition == 0 && oldItemPosition != 0 && oldItemPosition != oldPosHeadOther) {
                oldList[oldItemPosition - offsetOld].id == newList[newItemPosition - offsetNew].id
            } else if (oldPosHeadFav == -1 && newPosHeadFav == 0 && oldItemPosition == 0 && newItemPosition != 0 && newItemPosition != newPosHeadOther) {
                oldList[oldItemPosition - offsetOld].id == newList[newItemPosition - offsetNew].id
            } else {
                false
            }
        }

        return if (oldPosHeadOther != oldItemPosition && newPosHeadOther != newItemPosition) {
            oldList[oldItemPosition - offsetOld].id == newList[newItemPosition - offsetNew].id
        } else oldPosHeadOther == oldItemPosition && newPosHeadOther == newItemPosition
    }

    override fun getNewListSize() = getSize(newList)

    override fun getOldListSize() = getSize(oldList)


    private fun getSize(list: List<Task>): Int {
        return if (list.isNotEmpty()) {
            val posHeadOther = if (list[0].favorite) list.indexOfFirst { !it.favorite } + 1 else -1

            if (list[0].favorite && posHeadOther > 0) {
                list.size + 2
            } else {
                list.size
            }
        } else {
            list.size
        }
    }

    private fun recalculatePositions(oldItemPosition: Int, newItemPosition: Int) {
        oldPosHeadOther = if (oldList[0].favorite) oldList.indexOfFirst { !it.favorite } + 1 else -1
        oldPosHeadFav = if (oldPosHeadOther > 0) 0 else -1
        offsetOld = getOffset(oldPosHeadOther, oldItemPosition, oldList[0].favorite)

        newPosHeadOther = if (newList[0].favorite) newList.indexOfFirst { !it.favorite } + 1 else -1
        newPosHeadFav = if (newPosHeadOther > 0) 0 else -1
        offsetNew = getOffset(newPosHeadOther, newItemPosition, newList[0].favorite)
    }
}