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
import com.example.planner.enums.TaskKey
import com.example.planner.presenters.IMainPresenter
import com.example.planner.presenters.MainPresenter
import com.example.planner.task.Task
import com.example.planner.viewer.MainView
import kotlinx.android.synthetic.main.content_main.*

class FavoritesTasksFragment : Fragment(), MainView {
    private lateinit var adapterListFav: TaskArrayAdapter
    private lateinit var presenter: IMainPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fav_tasks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
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

    private fun init() {
        presenter = MainPresenter(this, requireContext(), LoaderManager.getInstance(this))

        adapterListFav = TaskArrayAdapter(requireContext(), presenter)

        taskFavListView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        taskFavListView.adapter = adapterListFav
    }

    override fun onListUpdate(tasks: Map<Int, Task>) {
        hideProgressBars()

        val groupMap = tasks.values.groupBy { it.favorite }

        adapterListFav.setList(groupMap[true] ?: listOf())
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
        progressBarFav.visibility = View.VISIBLE
    }

    private fun hideProgressBars() {
        progressBarFav.visibility = View.GONE
    }
}