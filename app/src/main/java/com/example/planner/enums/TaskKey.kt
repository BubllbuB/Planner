package com.example.planner.enums

enum class TaskKey {
    KEY_TASK {
        override fun getKey(): String = "task"

    },
    KEY_ACTION {
        override fun getKey(): String = "action"
    },
    KEY_TASK_FAV {
        override fun getKey(): String = "fav"
    };

    abstract fun getKey(): String
}