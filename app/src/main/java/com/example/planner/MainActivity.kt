package com.example.planner

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.ListView
import android.widget.TabHost
import com.example.planner.adapter.TaskAdapter
import com.example.planner.asyncLoaders.SharedLoader
import com.example.planner.presenters.MainPresenter
import com.example.planner.task.Task
import com.example.planner.viewer.MainView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

const val ADD_TASK = 1
const val EDIT_TASK = 2

class MainActivity : AppCompatActivity(), MainView, NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<SortedMap<Int, Task>> {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var presenter: MainPresenter
    private lateinit var taskAllAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        presenter = MainPresenter(this, this@MainActivity, this.applicationContext.resources)
        drawerLayout = findViewById(R.id.drawer_layout)

        fab.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra("Action", "Add")
            intent.putExtra("TitleActionBar", R.string.addTaskToolbarTitle)
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
        presenter.onUpdaterList()

        @Suppress("DEPRECATION")
        supportLoaderManager.initLoader(0, null, this@MainActivity).forceLoad()
    }

    override fun onCreateLoader(p0: Int, p1: Bundle?): Loader<SortedMap<Int, Task>> {
        return SharedLoader(this@MainActivity)
    }

    override fun onLoadFinished(p0: Loader<SortedMap<Int, Task>>, tasks: SortedMap<Int, Task>?) {
        val allListView = findViewById<ListView>(R.id.taskListView)
        val listTasks = arrayListOf<Task>()
        tasks?.values?.let {
            listTasks.addAll(it.toTypedArray())
        }
        taskAllAdapter = TaskAdapter(this, listTasks, presenter)
        allListView.adapter = taskAllAdapter
    }

    override fun onLoaderReset(p0: Loader<SortedMap<Int, Task>>) {
        val allListView = findViewById<ListView>(R.id.taskListView)
        allListView.adapter = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            presenter.onUpdaterList()
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
            R.id.nav_tasks -> {
            }
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

    override fun onListUpdate(tasks: SortedMap<Int, Task>?) {
        val allListView = findViewById<ListView>(R.id.taskListView)
        val favoriteListView = findViewById<ListView>(R.id.taskFavListView)

        val listTasks = arrayListOf<Task>()
        tasks?.values?.let {
            listTasks.addAll(it.toTypedArray())
        }
        taskAllAdapter = TaskAdapter(this, listTasks, presenter)
        allListView.adapter = taskAllAdapter

        val listFavoriteTasks = arrayListOf<Task>()
        listFavoriteTasks.addAll(listTasks.filter { it.favorite })
        val taskFavoriteAdapter = TaskAdapter(this, listFavoriteTasks, presenter)
        favoriteListView.adapter = taskFavoriteAdapter
    }

    override fun editSelectedTask(task: Task?) {
        val intent = Intent(this, AddTaskActivity::class.java)
        intent.putExtra("Action", "Edit")
        intent.putExtra("TitleActionBar", R.string.editTaskToolbarTitle)
        intent.putExtra("Task", task)
        startActivityForResult(intent, EDIT_TASK)
    }
}
