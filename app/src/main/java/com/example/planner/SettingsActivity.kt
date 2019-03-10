package com.example.planner

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.CheckBoxPreference
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.preference.PreferenceManager
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_settings.*


private lateinit var sCache: CheckBoxPreference
private lateinit var sShared: CheckBoxPreference
private lateinit var sInternal: CheckBoxPreference
private lateinit var sExternal: CheckBoxPreference
private lateinit var sDatabase: CheckBoxPreference

class SettingsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        drawerLayout = findViewById(R.id.drawer_layout_settings)

        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setTitle(R.string.settingsToolbarTitle)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        nav_viewSettings.setNavigationItemSelectedListener(this)
        PreferenceManager.setDefaultValues(this,R.xml.preferences,false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_tasks -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_settings -> {
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(p0: Bundle?, p1: String?) {
            addPreferencesFromResource(R.xml.preferences)

            sCache = findPreference("storageTypeCache") as CheckBoxPreference
            sShared = findPreference("storageTypeShared") as CheckBoxPreference
            sInternal = findPreference("storageTypeInternal") as CheckBoxPreference
            sExternal = findPreference("storageTypeExternal") as CheckBoxPreference
            sDatabase = findPreference("storageTypeDatabase") as CheckBoxPreference

            sCache.setOnPreferenceChangeListener{ _, _ ->
                if(!sCache.isChecked) {
                    setAllUnchecked()
                    sCache.isChecked = true
                }
                false
            }
            sShared.setOnPreferenceChangeListener{ _, _ ->
                if(!sShared.isChecked) {
                    setAllUnchecked()
                    sShared.isChecked = true
                }
                false
            }
            sInternal.setOnPreferenceChangeListener{ _, _ ->
                if(!sInternal.isChecked) {
                    setAllUnchecked()
                    sInternal.isChecked = true
                }
                false
            }
            sExternal.setOnPreferenceChangeListener{ _, _ ->
                if(!sExternal.isChecked) {
                    setAllUnchecked()
                    sExternal.isChecked = true
                }
                false
            }
            sDatabase.setOnPreferenceChangeListener{ _, _ ->
                if(!sDatabase.isChecked) {
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
}
