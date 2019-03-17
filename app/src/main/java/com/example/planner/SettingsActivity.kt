package com.example.planner

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceActivity
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.preference.CheckBoxPreference
import android.support.v7.preference.PreferenceManager
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_settings.*
import android.support.v7.app.AppCompatDelegate
import android.support.annotation.LayoutRes


@Suppress("DEPRECATION")
class SettingsActivity : PreferenceActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var sCache: CheckBoxPreference
    private lateinit var sShared: CheckBoxPreference
    private lateinit var sInternal: CheckBoxPreference
    private lateinit var sExternal: CheckBoxPreference
    private lateinit var sDatabase: CheckBoxPreference
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var mDelegate: AppCompatDelegate

    override fun onCreate(savedInstanceState: Bundle?) {
        getDelegate().installViewFactory()
        getDelegate().onCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        addPreferencesFromResource(R.xml.preferences)
        drawerLayout = findViewById(R.id.drawer_layout_settings)

        val actionbar: ActionBar? = getDelegate().supportActionBar
        actionbar?.apply {
            setTitle(R.string.settingsToolbarTitle)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        nav_viewSettings.setNavigationItemSelectedListener(this)
        PreferenceManager.setDefaultValues(this,R.xml.preferences,false)

        initCheckboxes()
    }

    private fun initCheckboxes() {
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
            true
        }
        sShared.setOnPreferenceChangeListener{ _, _ ->
            if(!sShared.isChecked) {
                setAllUnchecked()
                sShared.isChecked = true
            }
            true
        }
        sInternal.setOnPreferenceChangeListener{ _, _ ->
            if(!sInternal.isChecked) {
                setAllUnchecked()
                sInternal.isChecked = true
            }
            true
        }
        sExternal.setOnPreferenceChangeListener{ _, _ ->
            if(!sExternal.isChecked) {
                setAllUnchecked()
                sExternal.isChecked = true
            }
            true
        }
        sDatabase.setOnPreferenceChangeListener{ _, _ ->
            if(!sDatabase.isChecked) {
                setAllUnchecked()
                sDatabase.isChecked = true
            }
            true
        }
    }

    private fun setAllUnchecked() {
        sCache.isChecked = false
        sShared.isChecked = false
        sInternal.isChecked = false
        sExternal.isChecked = false
        sDatabase.isChecked = false
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        getDelegate().onPostCreate(savedInstanceState)
    }

    override fun setContentView(@LayoutRes layoutResID: Int) {
        getDelegate().setContentView(layoutResID)
    }

    override fun onPostResume() {
        super.onPostResume()
        getDelegate().onPostResume()
    }

    override fun onStop() {
        super.onStop()
        getDelegate().onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        getDelegate().onDestroy()
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
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun getDelegate(): AppCompatDelegate {
        if (!::mDelegate.isInitialized) {
            mDelegate = AppCompatDelegate.create(this, null)
        }
        return mDelegate
    }
}
