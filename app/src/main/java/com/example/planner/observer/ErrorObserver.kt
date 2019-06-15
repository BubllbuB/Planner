package com.example.planner.observer

interface ErrorObserver {
    fun showError(error: String, reload: Boolean)
}