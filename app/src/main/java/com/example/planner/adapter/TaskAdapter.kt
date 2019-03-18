package com.example.planner.adapter

import android.content.Context
import android.graphics.Paint
import android.support.v7.widget.PopupMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import com.example.planner.R
import com.example.planner.presenters.IMainPresenter
import com.example.planner.task.Task

const val TASK_REMOVE = 3
const val TASK_FAVORITE = 4
const val TASK_DONE = 5

class TaskAdapter(
    private val context: Context,
    private val taskList: ArrayList<Task>?,
    private val presenter: IMainPresenter
) : BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        val view: View
        val vh: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.list_item_task, parent, false)
            vh = ViewHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ViewHolder
        }

        vh.titleTextView?.text = taskList?.get(position)?.title
        vh.descriptionTextView?.text = taskList?.get(position)?.description

        taskList?.get(position)?.let {
            if (it.done) {
                vh.titleTextView?.paintFlags = ((vh.titleTextView?.paintFlags ?: 0)
                        or (Paint.STRIKE_THRU_TEXT_FLAG))
                vh.descriptionTextView?.paintFlags = ((vh.descriptionTextView?.paintFlags ?: 0)
                        or (Paint.STRIKE_THRU_TEXT_FLAG))
            }
        }

        vh.moreImageView?.setOnClickListener {
            showPopup(context, it, taskList?.get(position))
        }

        return view
    }

    override fun getItem(position: Int): Any? {
        return taskList?.get(position)
    }

    override fun getItemId(position: Int): Long {
        return taskList?.get(position)?.id?.toLong() ?: position.toLong()
    }

    override fun getCount(): Int {
        return taskList?.size ?: 0
    }

    private fun showPopup(context: Context, view: View, task: Task?) {
        val popup: PopupMenu?
        popup = PopupMenu(context, view)
        popup.inflate(R.menu.task)
        val favItem = popup.menu.findItem(R.id.favoriteTaskButton)
        val doneItem = popup.menu.findItem(R.id.doneTaskButton)
        task?.let {
            favItem.title = if (it.favorite) context.resources.getString(R.string.taskMenuRemoveFavorite)
            else context.resources.getString(R.string.taskMenuAddFavorite)

            doneItem.title = if (it.done) context.resources.getString(R.string.taskMenuUndone)
            else context.resources.getString(R.string.taskMenuDone)

            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
                when (item!!.itemId) {
                    R.id.editTaskButton -> {
                        presenter.editTask(task)
                    }
                    R.id.removeTaskButton -> {
                        presenter.updateTask(TASK_REMOVE, it)
                    }
                    R.id.favoriteTaskButton -> {
                        task.favorite = !task.favorite
                        presenter.updateTask(TASK_FAVORITE, task)
                    }
                    R.id.doneTaskButton -> {
                        task.done = !task.done
                        presenter.updateTask(TASK_DONE, task)
                    }
                }
                true
            })
        }
        popup.show()
    }

    private class ViewHolder(view: View?) {
        var titleTextView: TextView? = view?.findViewById(R.id.listTaskTitle)
        var descriptionTextView: TextView? = view?.findViewById(R.id.listTaskDescription)
        var moreImageView: Button? = view?.findViewById(R.id.listMoreButton)
    }
}