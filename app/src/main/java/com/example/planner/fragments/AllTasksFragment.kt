package com.example.planner.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.example.planner.R
import com.example.planner.adapter.TaskArrayAdapter
import com.example.planner.presenters.IMainPresenter
import com.example.planner.presenters.MainPresenter
import com.example.planner.task.Task
import com.example.planner.viewer.MainView

class AllTasksFragment : Fragment(), MainView {
    private lateinit var listViewAll: RecyclerView
    private lateinit var adapterListAll: TaskArrayAdapter
    private lateinit var progressBarAll: ProgressBar
    private lateinit var presenter: IMainPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_all_tasks, container, false)

        init(view)

        return view
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

    private fun init(view: View) {
        presenter = MainPresenter(this, requireContext(), LoaderManager.getInstance(this))

        listViewAll = view.findViewById(R.id.taskListView)
        progressBarAll = view.findViewById(R.id.progressBarAll)

        adapterListAll = TaskArrayAdapter(requireContext(), presenter)

        listViewAll.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        listViewAll.adapter = adapterListAll
    }

    override fun onListUpdate(tasks: Map<Int, Task>) {
        hideProgressBars()

        val tasksS = tasks.values.toList().sortedBy { !it.favorite }

        adapterListAll.setList(tasksS)
    }

    override fun editSelectedTask(task: Task?) {
        /*val intent = Intent(this, AddTaskFragment::class.java)
        intent.putExtra(TaskKey.KEY_TASK.getKey(), task)
        startActivityForResult(intent, TaskActionId.ACTION_EDIT.getId())*/
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.content_fragments, AddTaskFragment())
            .commit()
    }

    override fun showProgressBars() {
        progressBarAll.visibility = View.VISIBLE
    }

    private fun hideProgressBars() {
        progressBarAll.visibility = View.GONE
    }
}