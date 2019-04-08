package com.example.planner.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.planner.R
import com.example.planner.task.TaskFragmentSetter
import kotlinx.android.synthetic.main.content_main.*

class FavoritesTasksFragment : Fragment() {
    private lateinit var fragmentSetter: TaskFragmentSetter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fav_tasks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onStart() {
        super.onStart()
        fragmentSetter.onStart()
    }

    override fun onStop() {
        super.onStop()
        fragmentSetter.onStop()
    }

    private fun init() {
        fragmentSetter = TaskFragmentSetter(
            requireContext(),
            LoaderManager.getInstance(this),
            requireActivity().supportFragmentManager,
            progressBarFav, taskFavListView, true
        )
    }
}