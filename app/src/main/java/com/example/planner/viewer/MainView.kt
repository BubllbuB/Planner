package com.example.planner.viewer

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.planner.task.Task

@StateStrategyType(AddToEndSingleStrategy::class)
interface MainView: MvpView {
    fun onListUpdate(tasks: Map<Int, Task>)
    @StateStrategyType(SkipStrategy::class)
    fun editSelectedTask(task: Task?)
    fun showProgressBars()
    fun setAdapterSelectedPosition(position: Int)
}