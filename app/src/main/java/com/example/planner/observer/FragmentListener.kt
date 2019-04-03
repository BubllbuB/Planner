package com.example.planner.observer

import com.example.planner.R

interface FragmentListener {
    fun setupActionBar(title: String, iconId: Int = R.drawable.ic_menu) { }
}