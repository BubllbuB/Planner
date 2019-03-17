package com.example.planner

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.ListView
import android.widget.TabHost
import com.example.planner.adapter.TaskAdapter
import com.example.planner.presenters.IMainPresenter
import com.example.planner.presenters.MainPresenter
import com.example.planner.task.Task
import com.example.planner.viewer.MainView
import kotlinx.android.synthetic.main.activity_main.*

const val ADD_TASK = 1
const val EDIT_TASK = 2

class MainActivity : AppCompatActivity(), MainView {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var presenter: IMainPresenter
    private lateinit var listViewAll: ListView
    private lateinit var listViewFav: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        presenter = MainPresenter(this, this.applicationContext.resources)
        presenter.startListenStorage()
        drawerLayout = findViewById(R.id.drawer_layout)
        listViewAll = findViewById(R.id.taskListView)
        listViewFav = findViewById(R.id.taskFavListView)

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
    }

    override fun onStart() {
        presenter = MainPresenter(this, this.applicationContext.resources)
        presenter.startListenStorage()
        super.onStart()
    }

    override fun onDestroy() {
        presenter.stopListenStorage()
        super.onDestroy()
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

    private fun setNewTab(context: Context, tabHost: TabHost, tag: String, title: Int, icon: Int, contentID: Int) {
        val tabSpec = tabHost.newTabSpec(tag)
        tabSpec.apply {
            setContent(contentID)
            setIndicator(getString(title), context.resources.getDrawable(icon, null))
        }
        tabHost.addTab(tabSpec)
    }

    override fun onListUpdate(tasks: Map<Int, Task>) {
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
}
