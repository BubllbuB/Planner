package com.example.planner.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.planner.R
import com.example.planner.adapter.TaskArrayAdapter
import com.example.planner.enums.TaskKey
import com.example.planner.presenters.IMainPresenter
import com.example.planner.presenters.MainPresenter
import com.example.planner.task.Task
import com.example.planner.viewer.MainView
import kotlinx.android.synthetic.main.fragment_all_tasks.*

class AllTasksFragment : Fragment(), MainView {
    private lateinit var adapterListAll: TaskArrayAdapter
    private lateinit var presenter: IMainPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_all_tasks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        presenter = MainPresenter(this, requireContext(), LoaderManager.getInstance(this))

        adapterListAll = TaskArrayAdapter(requireContext(), presenter)

        taskListView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        taskListView.adapter = adapterListAll
    }

    override fun onStart() {
        super.onStart()
        presenter.updateFields(requireContext(), LoaderManager.getInstance(this))
        presenter.onStart()
        presenter.getTasksList()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun onListUpdate(tasks: Map<Int, Task>) {
        hideProgressBars()

        val tasksS = tasks.values.toList().sortedBy { !it.favorite }

        adapterListAll.setList(tasksS)
    }

    override fun editSelectedTask(task: Task?) {
        val fragment = AddTaskFragment()
        val bundle = Bundle()
        bundle.putParcelable(TaskKey.KEY_TASK.getKey(), task)
        fragment.arguments = bundle
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.content_fragments, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun showProgressBars() {
        progressBarAll.visibility = View.VISIBLE
    }

    private fun hideProgressBars() {
        progressBarAll.visibility = View.GONE
    }
}