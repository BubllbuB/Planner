package com.example.planner.enums

enum class TaskActionId {
    ACTION_ADD {
        override fun getId(): Int {
            return 1
        }
    },
    ACTION_REMOVE {
        override fun getId(): Int {
            return 2
        }
    },
    ACTION_EDIT {
        override fun getId(): Int {
            return 3
        }
    },
    ACTION_FAVORITE {
        override fun getId(): Int {
            return 4
        }
    },
    ACTION_DONE {
        override fun getId(): Int {
            return 5
        }
    };

    abstract fun getId(): Int
}