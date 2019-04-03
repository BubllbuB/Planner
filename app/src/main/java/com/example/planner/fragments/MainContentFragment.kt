package com.example.planner.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TabHost
import com.example.planner.FRAGMENT_TAG_ADD_TASK
import com.example.planner.R
import com.example.planner.enums.TaskKey
import kotlinx.android.synthetic.main.fragment_main.view.*

const val TAB_ALL = "tabAll"
const val TAB_FAV = "tabFavorite"

class MainContentFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        init(view)

        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.tab1, AllTasksFragment())
            .commit()

        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.tab2, FavoritesTasksFragment())
            .commit()

        return view
    }

    private fun init(view: View) {
        val tabHost = view.findViewById<TabHost>(R.id.tabHost)
        tabHost.setup()

        this.setNewTab(
            requireContext(),
            tabHost,
            TAB_ALL,
            R.string.textTabAllTasks,
            android.R.drawable.star_on,
            R.id.tab1
        )
        this.setNewTab(
            requireContext(),
            tabHost,
            TAB_FAV,
            R.string.textTabFavoriteTasks,
            android.R.drawable.star_on,
            R.id.tab2
        )


        view.fab.setOnClickListener {
            val fragment = AddTaskFragment()
            val bundle = Bundle()
            bundle.putBoolean(TaskKey.KEY_TASK_FAV.getKey(), tabHost.currentTabTag == TAB_FAV)
            fragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.content_fragments, fragment, FRAGMENT_TAG_ADD_TASK)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setNewTab(context: Context, tabHost: TabHost, tag: String, title: Int, icon: Int, contentID: Int) {
        val tabSpec = tabHost.newTabSpec(tag)
        tabSpec.apply {
            setContent(contentID)
            setIndicator(getString(title), context.resources.getDrawable(icon, null))
        }
        tabHost.addTab(tabSpec)
    }
}