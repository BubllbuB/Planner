package com.example.planner

import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.LoaderManager
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.planner.presenters.TaskPresenter
import com.example.planner.task.Task


class AddTaskActivity : AppCompatActivity() {
    private lateinit var presenter: TaskPresenter
    private lateinit var editTask: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        presenter = TaskPresenter(this, LoaderManager.getInstance(this))
        editTask = findViewById(R.id.taskTitleTextLayout)

        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            title = intent.getStringExtra("TitleActionBar")
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }
        val task = intent.getParcelableExtra<Task>("Task")
        editTask.editText?.setText(task?.title)
        findViewById<TextInputLayout>(R.id.taskDescriptionTextLayout).editText?.setText(task?.description)

        editTask.editText?.setOnClickListener {
            editTask.error = null
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
                val title = editTask.editText?.text.toString()
                val desc = findViewById<TextInputLayout>(R.id.taskDescriptionTextLayout).editText?.text.toString()
                if(title.isBlank()) {
                    editTask.error = "Title empty"
                    return false
                }

                when(intent.getStringExtra("Action")) {
                    "Add" -> {
                        val task = Task(title, desc)
                        presenter.updateTask(1, task)
                    }
                    "Edit" -> {
                        val task = intent.getParcelableExtra<Task>("Task")
                        task.title = title
                        task.description = desc
                        presenter.updateTask(2, task)
                    }
                }
                setResult(RESULT_OK)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
