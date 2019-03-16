package com.example.planner.storages

import android.content.Context
import android.support.v4.app.LoaderManager
import android.support.v7.preference.PreferenceManager
import java.lang.ref.WeakReference

class StorageFactory {
    companion object {
        fun getStorage(context: Context, loaderManager: LoaderManager):Storage {
            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            when {
                pref.getBoolean("storageTypeCache",true) -> return CacheStorage
                pref.getBoolean("storageTypeShared",false) -> return SharedPreferencesStorage.init(WeakReference(context), loaderManager)
                pref.getBoolean("storageTypeInternal",false) -> return InternalStorage.init(WeakReference(context), loaderManager)
                pref.getBoolean("storageTypeExternal",false) -> return ExternalStorage.init(WeakReference(context), loaderManager)
                pref.getBoolean("storageTypeDatabase",false) -> return CacheStorage
            }
            return CacheStorage
        }
    }
}