package com.example.planner.enums

enum class TaskKey {
    KEY_TASK {
        override fun getKey(): String {
            return "task"
        }
    },
    KEY_ACTION {
        override fun getKey(): String {
            return "action"
        }
    };

    abstract fun getKey(): String
}