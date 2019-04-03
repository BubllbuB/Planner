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

class FavoritesTasksFragment : Fragment(), MainView {
    private lateinit var listViewFav: RecyclerView
    private lateinit var adapterListFav: TaskArrayAdapter
    private lateinit var progressBarFav: ProgressBar
    private lateinit var presenter: IMainPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_fav_tasks, container, false)

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

        listViewFav = view.findViewById(R.id.taskFavListView)
        progressBarFav = view.findViewById(R.id.progressBarFav)

        adapterListFav = TaskArrayAdapter(requireContext(), presenter)

        listViewFav.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        listViewFav.adapter = adapterListFav
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