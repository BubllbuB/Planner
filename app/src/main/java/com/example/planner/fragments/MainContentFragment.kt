package com.example.planner.fragments

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.example.planner.FRAGMENT_TAG_ADD_TASK
import com.example.planner.R
import com.example.planner.adapter.TabAdapter
import com.example.planner.enums.TaskKey
import kotlinx.android.synthetic.main.fragment_main.*

const val ID_FAV_TAB = 1

class MainContentFragment : MvpAppCompatFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        val tabAdapter = TabAdapter(childFragmentManager)

        val allTaskFragment = AllTasksFragment()

        this.arguments?.getInt(ADAPTER_POSITION)?.let {
            val bundle = Bundle()
            bundle.putInt(ADAPTER_POSITION, it)
            allTaskFragment.arguments = bundle
        }

        tabAdapter.addFragment(allTaskFragment, getString(R.string.textTabAllTasks))
        tabAdapter.addFragment(FavoritesTasksFragment(), getString(R.string.textTabFavoriteTasks))

        viewPager.adapter = tabAdapter
        tabLayout.setupWithViewPager(viewPager)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(p0: TabLayout.Tab?) {
                fab.show()
            }

            override fun onTabReselected(p0: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {}
        })

        fab.setOnClickListener {
            val fragment = AddTaskFragment()
            val bundle = Bundle()
            bundle.putBoolean(TaskKey.KEY_TASK_FAV.getKey(), tabLayout.selectedTabPosition == ID_FAV_TAB)
            fragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.content_fragments, fragment, FRAGMENT_TAG_ADD_TASK)
                .addToBackStack(null)
                .commit()
        }
    }
}