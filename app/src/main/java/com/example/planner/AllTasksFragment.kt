package com.example.planner

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.example.planner.adapter.TaskArrayAdapter
import com.example.planner.presenters.IMainPresenter

class AllTasksFragment : Fragment() {
    private lateinit var listViewAll: RecyclerView
    private lateinit var adapterListAll: TaskArrayAdapter
    private lateinit var progressBarAll: ProgressBar
    private lateinit var presenter: IMainPresenter

    companion object{
        fun newInstance(presenter: IMainPresenter):AllTasksFragment{
            val args = Bundle()
            args.putSerializable("presenter", presenter)
            val fragment = AllTasksFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_all_tasks, container, false)

        presenter = arguments?.get("presenter") as IMainPresenter

        init(view)

        return view
    }

    private fun init(view: View) {


        listViewAll = view.findViewById(R.id.taskListView)
        progressBarAll = view.findViewById(R.id.progressBarAll)

        adapterListAll = TaskArrayAdapter(requireContext(), presenter)

        listViewAll.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        listViewAll.adapter = adapterListAll
    }
}