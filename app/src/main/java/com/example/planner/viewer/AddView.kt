package com.example.planner.viewer

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface AddView: MvpView {
    fun onTaskSaveSuccess()
    fun setTitle(title: String, startPos: Int, endPos: Int)
    fun setDescription(title: String, startPos: Int, endPos: Int)
    fun setFocus(focusId: Int = 0)
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun replaceFragment()
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun addFragment()
}