package com.example.planner

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TabHost
import com.example.planner.presenters.IMainPresenter
import kotlinx.android.synthetic.main.content_main.view.*

class MainContentFragment : Fragment() {
    private lateinit var presenter: IMainPresenter

    companion object{
        fun newInstance(presenter: IMainPresenter):MainContentFragment{
            val args = Bundle()
            args.putSerializable("presenter", presenter)
            val fragment = MainContentFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        init(view)

        presenter = arguments?.get("presenter") as IMainPresenter

        val allTasksFragment = AllTasksFragment.newInstance(presenter)

        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.tab1, allTasksFragment)
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
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.content_fragments, AddTaskFragment())
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