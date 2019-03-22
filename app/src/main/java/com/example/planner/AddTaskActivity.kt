package com.example.planner

import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.LoaderManager
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import com.example.planner.enums.TaskActionId
import com.example.planner.presenters.ITaskPresenter
import com.example.planner.presenters.TaskPresenter
import com.example.planner.task.Task
import com.example.planner.viewer.AddView


class AddTaskActivity : AppCompatActivity(), AddView {
    private lateinit var presenter: ITaskPresenter
    private lateinit var editTaskTitle: TextInputLayout
    private lateinit var editTaskDescription: TextInputLayout
    private var action = TaskActionId.ACTION_ADD.getId()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        init()
    }

    private fun init() {
        editTaskTitle = findViewById(R.id.taskTitleTextLayout)
        editTaskDescription = findViewById(R.id.taskDescriptionTextLayout)

        presenter = TaskPresenter(this, this@AddTaskActivity, LoaderManager.getInstance(this))

        val task = intent.getParcelableExtra<Task>("Task")
        if (task != null) action = TaskActionId.ACTION_EDIT.getId()

        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            title =
                if (action == TaskActionId.ACTION_ADD.getId()) getString(R.string.addTaskToolbarTitle)
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
        super.onStart()
        presenter.updateFields(this@AddTaskActivity, LoaderManager.getInstance(this))
        presenter.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onStop()
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
                    TaskActionId.ACTION_ADD.getId() -> {
                        val task = Task(title, desc)
                        presenter.updateTask(TaskActionId.ACTION_ADD.getId(), task)
                    }
                    TaskActionId.ACTION_EDIT.getId() -> {
                        val task = intent.getParcelableExtra<Task>("Task")
                        task.title = title
                        task.description = desc
                        presenter.updateTask(TaskActionId.ACTION_EDIT.getId(), task)
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
