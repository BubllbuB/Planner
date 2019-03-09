package com.example.planner

import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.planner.presenters.TaskPresenter
import com.example.planner.task.Task


class AddTaskActivity : AppCompatActivity() {
    private lateinit var presenter: TaskPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        presenter = TaskPresenter(this)

        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            title = intent.getStringExtra("TitleActionBar")
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.saveTaskButton -> {
                val title = findViewById<TextInputLayout>(R.id.taskTitleTextLayout).editText?.text.toString()
                val desc = findViewById<TextInputLayout>(R.id.taskDescriptionTextLayout).editText?.text.toString()
                val task = Task(title, desc)
                presenter.updateTask(1, task)
                setResult(RESULT_OK)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
