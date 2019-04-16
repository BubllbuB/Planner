package com.example.planner.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.view.View
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.planner.R
import com.example.planner.adapter.TaskArrayAdapter
import com.example.planner.enums.TaskKey
import com.example.planner.presenters.MainPresenter
import com.example.planner.task.Task
import com.example.planner.viewer.MainView

abstract class ListFragment : MvpAppCompatFragment(), MainView {
    @InjectPresenter
    lateinit var presenter: MainPresenter
    private lateinit var adapterList: TaskArrayAdapter

    @ProvidePresenter
    fun provideMainPresenter() = MainPresenter(requireContext(), LoaderManager.getInstance(this))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
    }

    abstract override fun showProgressBars()

    override fun onListUpdate(tasks: Map<Int, Task>) {
        hideProgressBars()
        adapterList.setList(getList(tasks))
    }

    override fun editSelectedTask(task: Task?) {
        val fragment = AddTaskFragment()
        val bundle = Bundle()
        bundle.putParcelable(TaskKey.KEY_TASK.getKey(), task)
        fragment.arguments = bundle

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.edit_fragment, fragment)
                .commit()
        } else {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.content_fragments, fragment)
                .addToBackStack(null)
                .commit()
        }
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

    private fun initAdapter() {
        adapterList = TaskArrayAdapter(requireContext(), presenter)
        init(adapterList)
    }

    override fun setAdapterSelectedPosition(position: Int) {
        adapterList.setSelectedPosition(position)
    }

    abstract fun init(adapter: TaskArrayAdapter)
    abstract fun hideProgressBars()
    abstract fun getList(tasks: Map<Int, Task>): List<Task>
}