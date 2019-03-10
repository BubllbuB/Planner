package com.example.planner.storages

import android.content.Context
import android.support.v7.preference.PreferenceManager

class StorageFactory {
    companion object {
        fun getStorage(context: Context):Storage {
            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            when {
                pref.getBoolean("storageTypeCache",true) -> return CacheStorage
                pref.getBoolean("storageTypeShared",false) -> return CacheStorage
                pref.getBoolean("storageTypeInternal",false) -> return CacheStorage
                pref.getBoolean("storageTypeExternal",false) -> return CacheStorage
                pref.getBoolean("storageTypeDatabase",false) -> return CacheStorage
            }
            return CacheStorage
        }
    }
}