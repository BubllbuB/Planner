package com.example.planner.adapter

import android.content.Context
import android.support.v7.widget.PopupMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import com.example.planner.R
import com.example.planner.presenters.MainPresenter
import com.example.planner.task.Task

class TaskAdapter(private val context: Context, private val taskList: ArrayList<Task>?, private val presenter: MainPresenter) : BaseAdapter() {

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
        taskList.let {
            vh.titleTextView.text = it?.get(position)?.title
            vh.descriptionTextView.text = it?.get(position)?.description
        }

        vh.moreImageView.setOnClickListener {
            showPopup(context, vh.moreImageView, taskList?.get(position))
        }

        return view
    }

    override fun getItem(position: Int): Any {
        return taskList?.get(position) ?: Any()
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

        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {
                R.id.editTaskButton -> {
                    presenter.editTask(task)
                }
                R.id.removeTaskButton -> {
                    presenter.updateTask(context.resources.getInteger(R.integer.taskRemove), task)
                }
            }
            true
        })
        popup.show()
    }

    private class ViewHolder(view: View?) {
        var titleTextView: TextView = view?.findViewById(R.id.listTaskTitle) as TextView
        var descriptionTextView: TextView = view?.findViewById(R.id.listTaskDescription) as TextView
        var moreImageView: Button = view?.findViewById(R.id.listMoreButton) as Button
    }
}