package com.example.planner.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.preference.CheckBoxPreference
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.preference.PreferenceManager
import android.view.View
import android.widget.FrameLayout
import com.example.planner.FRAGMENT_TAG_ADDTASK
import com.example.planner.R
import com.example.planner.observer.FragmentListener
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var sCache: CheckBoxPreference
    private lateinit var sShared: CheckBoxPreference
    private lateinit var sInternal: CheckBoxPreference
    private lateinit var sExternal: CheckBoxPreference
    private lateinit var sDatabase: CheckBoxPreference
    private lateinit var sFirebase: CheckBoxPreference
    private var mListener: FragmentListener? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, p1: String?) {
        addPreferencesFromResource(R.xml.preferences)
        PreferenceManager.setDefaultValues(requireContext(), R.xml.preferences, false)
        initCheckboxes()
    }

    fun setFragmentListener(callback: FragmentListener) {
        mListener = callback
    }

    override fun onStart() {
        super.onStart()

        if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE && this.isAdded) {
            val v = requireActivity().findViewById<FrameLayout>(R.id.content_fragments)
            v.layoutParams.width = ConstraintLayout.LayoutParams.MATCH_PARENT

            val v1 = requireActivity().findViewById<FrameLayout>(R.id.edit_fragment)
            v1.visibility = View.GONE
        }

        doAsync {
            val fragmentAdd = requireActivity().supportFragmentManager.findFragmentByTag(FRAGMENT_TAG_ADDTASK)
            fragmentAdd?.let {
                requireActivity().supportFragmentManager.beginTransaction().remove(it).commit()
            }
            uiThread {
                mListener?.setupActionBar(getString(R.string.settingsToolbarTitle))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mListener?.setupActionBar(getString(R.string.mainToolbarTitle))
        mListener = null
    }


    private fun initCheckboxes() {
        sCache = findPreference("storageTypeCache") as CheckBoxPreference
        sShared = findPreference("storageTypeShared") as CheckBoxPreference
        sInternal = findPreference("storageTypeInternal") as CheckBoxPreference
        sExternal = findPreference("storageTypeExternal") as CheckBoxPreference
        sDatabase = findPreference("storageTypeDatabase") as CheckBoxPreference
        sFirebase = findPreference("storageTypeFirebase") as CheckBoxPreference

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
        sFirebase.setOnPreferenceChangeListener { _, _ ->
            if (!sFirebase.isChecked) {
                setAllUnchecked()
                sFirebase.isChecked = true
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
        sFirebase.isChecked = false
    }
}
