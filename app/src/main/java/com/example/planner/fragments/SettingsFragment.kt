package com.example.planner.fragments

import android.os.Bundle
import android.support.v7.preference.CheckBoxPreference
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.preference.PreferenceManager
import com.example.planner.R

class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var sCache: CheckBoxPreference
    private lateinit var sShared: CheckBoxPreference
    private lateinit var sInternal: CheckBoxPreference
    private lateinit var sExternal: CheckBoxPreference
    private lateinit var sDatabase: CheckBoxPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, p1: String?) {
        addPreferencesFromResource(R.xml.preferences)
        PreferenceManager.setDefaultValues(requireContext(), R.xml.preferences, false)

        initCheckboxes()
    }

    private fun initCheckboxes() {
        sCache = findPreference("storageTypeCache") as CheckBoxPreference
        sShared = findPreference("storageTypeShared") as CheckBoxPreference
        sInternal = findPreference("storageTypeInternal") as CheckBoxPreference
        sExternal = findPreference("storageTypeExternal") as CheckBoxPreference
        sDatabase = findPreference("storageTypeDatabase") as CheckBoxPreference

        sCache.setOnPreferenceChangeListener { _, _ ->
            if (!sCache.isChecked) {
                setAllUnchecked()
                sCache.isChecked = true
            }
            false
        }
        sShared.setOnPreferenceChangeListener { _, _ ->
            if (!sShared.isChecked) {
                setAllUnchecked()
                sShared.isChecked = true
            }
            false
        }
        sInternal.setOnPreferenceChangeListener { _, _ ->
            if (!sInternal.isChecked) {
                setAllUnchecked()
                sInternal.isChecked = true
            }
            false
        }
        sExternal.setOnPreferenceChangeListener { _, _ ->
            if (!sExternal.isChecked) {
                setAllUnchecked()
                sExternal.isChecked = true
            }
            false
        }
        sDatabase.setOnPreferenceChangeListener { _, _ ->
            if (!sDatabase.isChecked) {
                setAllUnchecked()
                sDatabase.isChecked = true
            }
            false
        }
    }

    private fun setAllUnchecked() {
        sCache.isChecked = false
        sShared.isChecked = false
        sInternal.isChecked = false
        sExternal.isChecked = false
        sDatabase.isChecked = false
    }
}
