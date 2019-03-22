package com.example.planner.enums

enum class TaskActionId {
    ACTION_ADD {
        override fun getId(): Int = 1

    },
    ACTION_REMOVE {
        override fun getId(): Int = 2

    },
    ACTION_EDIT {
        override fun getId(): Int = 3

    },
    ACTION_FAVORITE {
        override fun getId(): Int = 4

    },
    ACTION_DONE {
        override fun getId(): Int = 5

    };

    abstract fun getId(): Int
}