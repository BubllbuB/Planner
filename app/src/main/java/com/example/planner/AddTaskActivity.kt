package com.example.planner

import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.planner.presenters.ITaskPresenter
import com.example.planner.presenters.TaskPresenter
import com.example.planner.task.Task
import com.example.planner.viewer.AddView


class AddTaskActivity : AppCompatActivity(), AddView {
    private lateinit var presenter: ITaskPresenter
    private lateinit var editTask: TextInputLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        presenter = TaskPresenter(this)
        editTask = findViewById(R.id.taskTitleTextLayout)
        presenter.startListenStorage()

        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            title = intent.getStringExtra("TitleActionBar")
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        val task = intent.getParcelableExtra<Task>("Task")
        editTask.editText?.setText(task?.title)
        findViewById<TextInputLayout>(R.id.taskDescriptionTextLayout).editText?.setText(task?.description)

        editTask.editText?.setOnClickListener {
            editTask.error = null
        }
    }

    override fun onStart() {
        presenter = TaskPresenter(this)
        presenter.startListenStorage()
        super.onStart()
    }

    override fun onDestroy() {
        presenter.stopListenStorage()
        super.onDestroy()
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
                val task = Task(title, desc)
                presenter.updateTask(1, task)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onTaskSaveSuccess() {
        setResult(RESULT_OK)
        finish()
    }
}
