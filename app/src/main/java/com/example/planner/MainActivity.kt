package com.example.planner

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.LoaderManager
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.PreferenceManager
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TabHost
import android.widget.Toast
import com.example.planner.adapter.TaskAdapter
import com.example.planner.presenters.IMainPresenter
import com.example.planner.presenters.MainPresenter
import com.example.planner.task.Task
import com.example.planner.viewer.MainView
import kotlinx.android.synthetic.main.activity_main.*

const val ADD_TASK = 1
const val EDIT_TASK = 2
const val CHECK_REQUEST = 3
const val PREF_SHARED = "storageTypeShared"
const val PREF_EXTERNAL = "storageTypeExternal"

class MainActivity : AppCompatActivity(), MainView, NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var presenter: IMainPresenter
    private lateinit var listViewAll: ListView
    private lateinit var listViewFav: ListView
    private lateinit var progressBarAll: ProgressBar
    private lateinit var progressBarFav: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        init()

        drawerLayout = findViewById(R.id.drawer_layout)
    }

    private fun init() {
        drawerLayout = findViewById(R.id.drawer_layout)
        listViewAll = findViewById(R.id.taskListView)
        listViewFav = findViewById(R.id.taskFavListView)
        progressBarAll = findViewById(R.id.progressBarAll)
        progressBarFav = findViewById(R.id.progressBarFav)

        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        fab.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivityForResult(intent, ADD_TASK)
        }

        val tabHost = findViewById<TabHost>(R.id.tabHost)
        tabHost.setup()

        this.setNewTab(
            this@MainActivity,
            tabHost,
            "tabAll",
            R.string.textTabAllTasks,
            android.R.drawable.star_on,
            R.id.tab1
        )
        this.setNewTab(
            this@MainActivity,
            tabHost,
            "tabFavorite",
            R.string.textTabFavoriteTasks,
            android.R.drawable.star_on,
            R.id.tab2
        )

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onStart() {
        super.onStart()
        presenter = MainPresenter(this, this@MainActivity, LoaderManager.getInstance(this))
        presenter.onStart()
        checkPermissions()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            presenter.getTasksList()
        }
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
            R.id.nav_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setNewTab(context: Context, tabHost: TabHost, tag: String, title: Int, icon: Int, contentID: Int) {
        val tabSpec = tabHost.newTabSpec(tag)
        tabSpec.apply {
            setContent(contentID)
            setIndicator(getString(title), context.resources.getDrawable(icon, null))
        }
        tabHost.addTab(tabSpec)
    }

    override fun onListUpdate(tasks: Map<Int, Task>) {
        hideProgressBars()

        val listTasks = arrayListOf<Task>()
        listTasks.addAll(tasks.values.toTypedArray())
        listViewAll.adapter = TaskAdapter(this, listTasks, presenter)

        val listFavoriteTasks = arrayListOf<Task>()
        listFavoriteTasks.addAll(listTasks.filter{ it.favorite })
        listViewFav.adapter = TaskAdapter(this, listFavoriteTasks, presenter)
    }

    override fun editSelectedTask(task: Task?) {
        val intent = Intent(this, AddTaskActivity::class.java)
        intent.putExtra("Task", task)
        startActivityForResult(intent, EDIT_TASK)
    }

    override fun showProgressBars() {
        listViewAll.adapter = null
        listViewFav.adapter = null
        progressBarAll.visibility = View.VISIBLE
        progressBarFav.visibility = View.VISIBLE
    }

    private fun hideProgressBars() {
        progressBarAll.visibility = View.GONE
        progressBarFav.visibility = View.GONE
    }

    private fun checkPermissions() {
        val pref = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
        if (pref.getBoolean(PREF_EXTERNAL, false)) {
            val permissionWrite = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )

            if (permissionWrite != PackageManager.PERMISSION_GRANTED) {
                makeRequest()
            } else {
                presenter.getTasksList()
            }
        } else {
            presenter.getTasksList()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            CHECK_REQUEST
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            CHECK_REQUEST -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    val pref = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
                    val editor = pref.edit()
                    editor.putBoolean(PREF_SHARED, true)
                    editor.putBoolean(PREF_EXTERNAL, false)
                    editor.apply()
                    Toast.makeText(this@MainActivity, R.string.error_external_permission, Toast.LENGTH_SHORT).show()
                    presenter = MainPresenter(this, this@MainActivity, LoaderManager.getInstance(this))
                    presenter.onStart()
                    presenter.getTasksList()
                } else {
                    presenter.getTasksList()
                }
            }
        }
    }
}
