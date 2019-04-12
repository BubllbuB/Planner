package com.example.planner.task

import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.LoaderManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import com.arellomobile.mvp.MvpDelegate
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.planner.R
import com.example.planner.adapter.TaskArrayAdapter
import com.example.planner.enums.TaskKey
import com.example.planner.fragments.AddTaskFragment
import com.example.planner.presenters.MainPresenter
import com.example.planner.presenters.TaskPresenter
import com.example.planner.viewer.MainView

class TaskFragmentSetter(
    private val context: Context,
    private val loaderManager: LoaderManager,
    private val fragmentManager: FragmentManager,
    private val progressBar: ProgressBar,
    tasksRecyclerView: RecyclerView,
    private val favorites: Boolean = false
) : MainView {
    private var presenter = MainPresenter( context, loaderManager)
    private val adapterList = TaskArrayAdapter(context, presenter)

    @ProvidePresenter
    fun provideMainPresenter() = MainPresenter(context, loaderManager)

    init {
        tasksRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        tasksRecyclerView.adapter = adapterList
    }

    fun onStart() {
        presenter.updateFields(context, loaderManager)
        presenter.onStart()
        presenter.getTasksList()
    }

    fun onStop() {
        presenter.onStop()
    }

    override fun editSelectedTask(task: Task?) {
        val fragment = AddTaskFragment()
        val bundle = Bundle()
        bundle.putParcelable(TaskKey.KEY_TASK.getKey(), task)
        fragment.arguments = bundle
        fragmentManager.beginTransaction()
            .replace(R.id.content_fragments, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onListUpdate(tasks: Map<Int, Task>) {
        hideProgressBars()
        adapterList.setList(getList(tasks))
    }

    override fun showProgressBars() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBars() {
        progressBar.visibility = View.GONE
    }

    private fun getList(tasks: Map<Int, Task>): List<Task> {
        return if (favorites) tasks.values.groupBy { it.favorite }[true] ?: listOf()
        else tasks.values.toList().sortedBy { !it.favorite }
    }
}