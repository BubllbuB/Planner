package com.example.planner.presenters

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.planner.viewer.ActivityView

@InjectViewState
class ActivityPresenter : MvpPresenter<ActivityView>() {
    fun onSetContent(savedInstanceState: Bundle?) {
        viewState.setContentFragment(savedInstanceState)
    }
}