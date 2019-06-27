package com.example.planner.fragments

import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.planner.MainActivity
import com.example.planner.R
import com.example.planner.adapter.TaskArrayAdapter
import com.example.planner.enums.TaskKey
import com.example.planner.task.Task
import kotlinx.android.synthetic.main.fragment_all_tasks.*

class AllTasksFragment : ListFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_all_tasks, container, false)
    }

    override fun onStart() {
        super.onStart()
        presenter.onSubscribeError()
    }

    override fun onStop() {
        super.onStop()
        presenter.onUnsubscribeError()
    }

    override fun init(adapter: TaskArrayAdapter) {
        taskListView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        taskListView.adapter = adapter
    }

    override fun bundlePutTask(task: Task?): Bundle {
        val bundle = Bundle()
        bundle.putParcelable(TaskKey.KEY_TASK.getKey(), task)
        return bundle
    }

    override fun savePosition(position: Int) {
        val bundlePosition = Bundle()
        bundlePosition.putInt(ADAPTER_POSITION_ALL, position)
        this.arguments = bundlePosition
    }

    override fun checkSavedPosition() {
        this.arguments?.getInt(ADAPTER_POSITION_ALL)?.let {
            presenter.updateAdapterPosition(it)
        }
    }

    override fun checkNotificationDetails() {
        val activity = requireActivity() as MainActivity
        val intentActivity = activity.checkBundle()

        if (intentActivity.extras != null && !intentActivity.hasExtra("DONE") && intentActivity.hasExtra("task")) {
            val task = intentActivity.getParcelableExtra<Task>("task")
            intentActivity.putExtra("DONE", 0)
            presenter.onNotificationDetails(task)
        }
    }

    override fun onError(message: String, reload: Boolean) {
        if (this.isVisible) {

            super.onError(message, reload)

            if (reload) {
                presenter.reloadStorage()
            }

            val coordinatorLayout = requireActivity().findViewById<CoordinatorLayout>(R.id.main_fragment_layout)
            Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show()
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