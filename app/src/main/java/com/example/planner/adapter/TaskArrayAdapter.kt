package com.example.planner.adapter

import android.content.Context
import android.graphics.Paint
import android.support.v7.widget.PopupMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.example.planner.R
import com.example.planner.enums.TaskActionId
import com.example.planner.presenters.IMainPresenter
import com.example.planner.task.Task

class TaskArrayAdapter(
    context: Context,
    private val taskList: List<Task>,
    private val presenter: IMainPresenter
) : ArrayAdapter<Task>(context, 0, taskList) {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return taskList.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
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

        vh.titleTextView?.text = taskList[position].title
        vh.descriptionTextView?.text = taskList[position].description

        if (taskList[position].done) {
            vh.titleTextView?.paintFlags = ((vh.titleTextView?.paintFlags ?: 0)
                    or (Paint.STRIKE_THRU_TEXT_FLAG))
            vh.descriptionTextView?.paintFlags = ((vh.descriptionTextView?.paintFlags ?: 0)
                    or (Paint.STRIKE_THRU_TEXT_FLAG))
        }

        vh.moreImageView?.setOnClickListener {
            showPopup(context, it, taskList[position])
        }

        return view
    }

    private fun showPopup(context: Context, view: View, task: Task) {
        val popup: PopupMenu?
        popup = PopupMenu(context, view)
        popup.inflate(R.menu.task)
        val favItem = popup.menu.findItem(R.id.favoriteTaskButton)
        val doneItem = popup.menu.findItem(R.id.doneTaskButton)
        favItem.title = if (task.favorite) context.resources.getString(R.string.taskMenuRemoveFavorite)
        else context.resources.getString(R.string.taskMenuAddFavorite)

        doneItem.title = if (task.done) context.resources.getString(R.string.taskMenuUndone)
        else context.resources.getString(R.string.taskMenuDone)

        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.editTaskButton -> {
                    presenter.editTask(task)
                }
                R.id.removeTaskButton -> {
                    presenter.updateTask(TaskActionId.ACTION_REMOVE.getId(), task)
                }
                R.id.favoriteTaskButton -> {
                    task.favorite = !task.favorite
                    presenter.updateTask(TaskActionId.ACTION_FAVORITE.getId(), task)
                }
                R.id.doneTaskButton -> {
                    task.done = !task.done
                    presenter.updateTask(TaskActionId.ACTION_DONE.getId(), task)
                }
            }
            true
        })

        popup.show()
    }

    private class ViewHolder(view: View?) {
        var titleTextView: TextView? = view?.findViewById(R.id.listTaskTitle)
        var descriptionTextView: TextView? = view?.findViewById(R.id.listTaskDescription)
        var moreImageView: Button? = view?.findViewById(R.id.listMoreButton)
    }
}