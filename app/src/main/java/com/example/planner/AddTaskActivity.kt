package com.example.planner

import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import com.example.planner.presenters.ITaskPresenter
import com.example.planner.presenters.TaskPresenter
import com.example.planner.task.Task
import com.example.planner.viewer.AddView

const val TASK_ADD = 1
const val TASK_EDIT = 2

class AddTaskActivity : AppCompatActivity(), AddView {
    private lateinit var presenter: ITaskPresenter
    private lateinit var editTaskTitle: TextInputLayout
    private lateinit var editTaskDescription: TextInputLayout
    private var action = TASK_ADD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        editTaskTitle = findViewById(R.id.taskTitleTextLayout)
        editTaskDescription = findViewById(R.id.taskDescriptionTextLayout)

        presenter = TaskPresenter(this, this@AddTaskActivity, supportLoaderManager)
        presenter.startListenStorage()

        val task = intent.getParcelableExtra<Task>("Task")
        if (task != null) action = TASK_EDIT

        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            title =
                if (action == TASK_ADD) getString(R.string.addTaskToolbarTitle)
                else getString(R.string.editTaskToolbarTitle)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }


        editTaskTitle.editText?.setText(task?.title)
        editTaskDescription.editText?.setText(task?.description)

        editTaskTitle.editText?.setOnClickListener {
            editTaskTitle.error = null
        }

        editTaskTitle.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                editTaskTitle.error = null
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    override fun onStart() {
        presenter = TaskPresenter(this, this@AddTaskActivity)
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
                val title = editTaskTitle.editText?.text.toString()
                val desc = editTaskDescription.editText?.text.toString()

                if (title.isBlank()) {
                    editTaskTitle.error = "Title empty"
                    return true
                }

                when (action) {
                    TASK_ADD -> {
                        val task = Task(title, desc)
                        presenter.updateTask(1, task)
                    }
                    TASK_EDIT -> {
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

    override fun onTaskSaveSuccess() {
        setResult(RESULT_OK)
        finish()
    }
}
