package com.example.planner.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.view.View
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.planner.FRAGMENT_TAG_ADDTASK
import com.example.planner.MainActivity
import com.example.planner.R
import com.example.planner.adapter.TaskArrayAdapter
import com.example.planner.presenters.MainPresenter
import com.example.planner.task.Task
import com.example.planner.viewer.MainView

const val ADAPTER_POSITION_ALL = "adapterPositionAll"
const val ADAPTER_POSITION_FAV = "adapterPositionFav"

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
        checkSavedPosition()
    }

    override fun editSelectedTask(task: Task?) {
        val fragmentAdd = requireActivity().supportFragmentManager.findFragmentByTag(FRAGMENT_TAG_ADDTASK)
        val fragmentArguments = bundlePutTask(task)

        if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setNewAddFragment(fragmentArguments)
        } else if (fragmentAdd == null || !fragmentAdd.isAdded) {
            if(!(requireActivity() as MainActivity).existAddFragment) {
                setNewAddFragment(fragmentArguments)
            }
        } else {
            fragmentAdd as AddTaskFragment
            fragmentAdd.arguments = fragmentArguments
            fragmentAdd.setTask()
        }
    }

    private fun setNewAddFragment(arguments: Bundle) {
        val fragmentAdd = AddTaskFragment()
        fragmentAdd.arguments = arguments

        if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.edit_fragment, fragmentAdd, FRAGMENT_TAG_ADDTASK)
                .commit()
        } else if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.content_fragments, fragmentAdd, FRAGMENT_TAG_ADDTASK)
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
        checkSavedPosition()
        init(adapterList)
    }

    override fun setAdapterSelectedPosition(position: Int) {
        adapterList.setSelectedPosition(position)
        savePosition(adapterList.getSelectedPosition())
    }

    override fun setAdapterStartPosition() {
        adapterList.setSelectedStartedPosition()
        savePosition(adapterList.getSelectedPosition())
    }

    fun adapterExist(): Boolean {
        return ::adapterList.isInitialized
    }

    override fun showDetails(task: Task) {
        adapterList.setSelectedTask(task)
    }

    override fun onError(message: String, reload: Boolean) {}

    abstract override fun checkNotificationDetails()
    abstract fun checkSavedPosition()
    abstract fun savePosition(position: Int)
    abstract fun bundlePutTask(task: Task?): Bundle
    abstract fun init(adapter: TaskArrayAdapter)
    abstract fun hideProgressBars()
    abstract fun getList(tasks: Map<Int, Task>): List<Task>
}