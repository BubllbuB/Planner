package com.example.planner.fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.planner.R
import com.example.planner.adapter.TaskArrayAdapter
import com.example.planner.enums.TaskKey
import com.example.planner.task.Task
import kotlinx.android.synthetic.main.fragment_all_tasks.*

class AllTasksFragment : ListFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_all_tasks, container, false)
    }

    override fun init(adapter: TaskArrayAdapter) {
        taskListView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        taskListView.adapter = adapter
    }

    override fun bundlePutPosition(task: Task?, position: Int): Bundle {
        val bundle = Bundle()
        bundle.putParcelable(TaskKey.KEY_TASK.getKey(), task)
        bundle.putInt(ADAPTER_POSITION_ALL, position)
        return bundle
    }

    override fun checkSavedPosition(bundle: Bundle?) {
        bundle?.getInt(ADAPTER_POSITION_ALL)?.let {
            presenter.updateAdapterPosition(it)
        }
    }

    override fun showProgressBars() {
        progressBarAll.visibility = View.VISIBLE
    }

    override fun hideProgressBars() {
        progressBarAll.visibility = View.GONE
    }

    override fun getList(tasks: Map<Int, Task>): List<Task> = tasks.values.toList().sortedBy { !it.favorite }
}