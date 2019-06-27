package com.example.planner.storages

import android.content.Context
import android.support.v4.app.LoaderManager
import android.support.v7.preference.PreferenceManager
import java.lang.ref.WeakReference

const val STORAGE_TYPE_CACHE = "storageTypeCache"
const val STORAGE_TYPE_SHARED = "storageTypeShared"
const val STORAGE_TYPE_INTERNAL = "storageTypeInternal"
const val STORAGE_TYPE_EXTERNAL = "storageTypeExternal"
const val STORAGE_TYPE_DATABASE = "storageTypeDatabase"
const val STORAGE_TYPE_FIREBASE = "storageTypeFirebase"

class StorageFactory {
    companion object {
        fun getStorage(context: Context, loaderManager: LoaderManager): Storage {
            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            when {
                pref.getBoolean(STORAGE_TYPE_CACHE, true) -> return CacheStorage
                pref.getBoolean(STORAGE_TYPE_SHARED, false) -> return SharedPreferencesStorage.init(
                    WeakReference(
                        context
                    ), loaderManager
                )
                pref.getBoolean(STORAGE_TYPE_INTERNAL, false) -> return InternalStorage.init(
                    WeakReference(context),
                    loaderManager
                )
                pref.getBoolean(STORAGE_TYPE_EXTERNAL, false) -> return ExternalStorage.init(
                    WeakReference(context),
                    loaderManager
                )
                pref.getBoolean(STORAGE_TYPE_DATABASE, false) -> return DatabaseStorage.init(
                    WeakReference(context),
                    loaderManager
                )
                pref.getBoolean(STORAGE_TYPE_FIREBASE, false) -> return FirebaseStorage.init(WeakReference(context))
            }
            return CacheStorage
        }
    }
}