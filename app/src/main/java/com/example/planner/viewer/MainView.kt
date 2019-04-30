package com.example.planner.viewer

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.*
import com.example.planner.task.Task

@StateStrategyType(AddToEndSingleStrategy::class)
interface MainView: MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onListUpdate(tasks: Map<Int, Task>)

    @StateStrategyType(SkipStrategy::class)
    fun editSelectedTask(task: Task?)

    fun showProgressBars()

    fun setAdapterSelectedPosition(position: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setAdapterStartPosition()
}