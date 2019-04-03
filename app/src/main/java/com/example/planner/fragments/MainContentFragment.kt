package com.example.planner.fragments

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.planner.FRAGMENT_TAG_ADD_TASK
import com.example.planner.R
import com.example.planner.adapter.TabAdapter
import com.example.planner.enums.TaskKey
import kotlinx.android.synthetic.main.fragment_main.view.*

const val ID_FAV_TAB = 1

class MainContentFragment : Fragment() {
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        init(view)

        return view
    }

    private fun init(view: View) {
        viewPager = view.findViewById(R.id.viewPager)
        tabLayout = view.findViewById(R.id.tabLayout)

        val tabAdapter = TabAdapter(childFragmentManager)
        tabAdapter.addFragment(AllTasksFragment(), getString(R.string.textTabAllTasks))
        tabAdapter.addFragment(FavoritesTasksFragment(), getString(R.string.textTabFavoriteTasks))

        viewPager.adapter = tabAdapter
        tabLayout.setupWithViewPager(viewPager)

        view.fab.setOnClickListener {
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