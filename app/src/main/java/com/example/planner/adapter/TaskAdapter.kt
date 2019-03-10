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

const val TASK_EDIT = 2
const val TASK_REMOVE = 3
const val TASK_FAVORITE = 4

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
        taskList.let { list ->
            vh.titleTextView.text = list?.get(position)?.title
            vh.descriptionTextView.text = list?.get(position)?.description

            list?.get(position)?.let {
                if(it.done) {
                    vh.titleTextView.paintFlags = (vh.titleTextView.paintFlags
                            or(Paint.STRIKE_THRU_TEXT_FLAG))
                    vh.descriptionTextView.paintFlags = (vh.descriptionTextView.paintFlags
                            or(Paint.STRIKE_THRU_TEXT_FLAG))
                }
            }
        }

        vh.titleTextView?.text = taskList?.get(position)?.title
        vh.descriptionTextView?.text = taskList?.get(position)?.description

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
        }

        task?.let {
            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
            if(!it.favorite) favItem.title = context.resources.getString(R.string.taskMenuAddFavorite)
            else favItem.title = context.resources.getString(R.string.taskMenuRemoveFavorite)

            if(!it.done) doneItem.title = context.resources.getString(R.string.taskMenuDone)
            else doneItem.title = context.resources.getString(R.string.taskMenuUndone)
        }

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
                        task?.let {
                            it.done = !it.done
                        }
                        presenter.updateTask(context.resources.getInteger(R.integer.setDone), task)
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