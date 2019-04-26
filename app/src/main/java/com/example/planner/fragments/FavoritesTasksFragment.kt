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
import kotlinx.android.synthetic.main.content_main.*

class FavoritesTasksFragment : ListFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fav_tasks, container, false)
    }

    override fun init(adapter: TaskArrayAdapter) {
        taskFavListView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        taskFavListView.adapter = adapter
    }

    override fun bundlePutPosition(task: Task?, position: Int): Bundle {
        val bundle = Bundle()
        bundle.putParcelable(TaskKey.KEY_TASK.getKey(), task)
        bundle.putInt(ADAPTER_POSITION_FAV, position)
        return bundle
    }

    override fun checkSavedPosition(bundle: Bundle?) {
        bundle?.getInt(ADAPTER_POSITION_FAV)?.let {
            presenter.updateAdapterPosition(it)
        }
    }

    override fun showProgressBars() {
        progressBarFav.visibility = View.VISIBLE
    }

    override fun hideProgressBars() {
        progressBarFav.visibility = View.GONE
    }

    override fun getList(tasks: Map<Int, Task>): List<Task> = tasks.values.groupBy { it.favorite }[true] ?: listOf()
}