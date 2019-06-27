package com.example.planner.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
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

    override fun onStart() {
        super.onStart()

        if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE && this.isVisible) {
            val v = requireActivity().findViewById<FrameLayout>(R.id.content_fragments)
            v.layoutParams.width = 0

            val v1 = requireActivity().findViewById<FrameLayout>(R.id.edit_fragment)
            v1.visibility = View.VISIBLE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        val tabAdapter = TabAdapter(childFragmentManager)

        val fragmentAllTasks =
            childFragmentManager.findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + 0) as ListFragment?
        val fragmentFavTasks =
            childFragmentManager.findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + 1) as ListFragment?

        val allTaskFragment = if (fragmentAllTasks != null && fragmentAllTasks.adapterExist()) {
            fragmentAllTasks
        } else {
            AllTasksFragment()
        }


        val favTaskFragment = if (fragmentFavTasks != null && fragmentFavTasks.adapterExist()) {
            fragmentFavTasks
        } else {
            FavoritesTasksFragment()
        }

        this.arguments?.getInt(ADAPTER_POSITION_ALL)?.let {
            val bundle = Bundle()
            bundle.putInt(ADAPTER_POSITION_ALL, it)
            allTaskFragment.arguments = bundle
        }

        this.arguments?.getInt(ADAPTER_POSITION_FAV)?.let {
            val bundle = Bundle()
            bundle.putInt(ADAPTER_POSITION_FAV, it)
            favTaskFragment.arguments = bundle
        }

        tabAdapter.addFragment(allTaskFragment, getString(R.string.textTabAllTasks))
        tabAdapter.addFragment(favTaskFragment, getString(R.string.textTabFavoriteTasks))

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

            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.edit_fragment, fragment)
                    .commit()
            } else {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.content_fragments, fragment, FRAGMENT_TAG_ADD_TASK)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}