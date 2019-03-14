package com.example.planner.storages

import android.content.Context
import android.support.v4.app.LoaderManager
import android.support.v7.preference.PreferenceManager
import com.example.planner.task.Task

class StorageFactory {
    companion object {
        fun getStorage(context: Context, loaderManager: LoaderManager):Storage {
            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            when {
                pref.getBoolean("storageTypeCache",true) -> return CacheStorage
                pref.getBoolean("storageTypeShared",false) -> return SharedPreferencesStorage(context, loaderManager, sortedMapOf())
                pref.getBoolean("storageTypeInternal",false) -> return CacheStorage
                pref.getBoolean("storageTypeExternal",false) -> return CacheStorage
                pref.getBoolean("storageTypeDatabase",false) -> return CacheStorage
            }
            return CacheStorage
        }
    }
}