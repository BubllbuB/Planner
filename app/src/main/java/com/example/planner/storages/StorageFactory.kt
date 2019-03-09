package com.example.planner.storages

class StorageFactory {
    companion object {
        fun getStorage():Storage {
            return CacheStorage
        }
    }
}