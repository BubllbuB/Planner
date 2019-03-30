package com.example.planner.adapter

import android.content.Context
import android.support.v7.widget.LinearLayoutManager

class LinearLayoutManagerWrapper(private val context: Context): LinearLayoutManager(context) {

    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }

}