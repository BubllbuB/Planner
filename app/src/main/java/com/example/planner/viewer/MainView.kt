package com.example.planner.viewer

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.*
import com.example.planner.task.Task

@StateStrategyType(AddToEndSingleStrategy::class)
interface MainView: MvpView {
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onListUpdate(tasks: Map<Int, Task>)

    @StateStrategyType(SkipStrategy::class)
    fun editSelectedTask(task: Task?)

    fun showProgressBars()

    @StateStrategyType(SkipStrategy::class)
    fun setAdapterSelectedPosition(position: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setAdapterStartPosition()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showDetails(task: Task)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun checkNotificationDetails()

    @StateStrategyType(SkipStrategy::class)
    fun onReloadStorage()

    @StateStrategyType(SkipStrategy::class)
    fun onError(message: String, reload: Boolean)
}