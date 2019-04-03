package com.example.planner.fragments

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import com.example.planner.R
import com.example.planner.enums.TaskActionId
import com.example.planner.enums.TaskKey
import com.example.planner.observer.FragmentListener
import com.example.planner.presenters.ITaskPresenter
import com.example.planner.presenters.TaskPresenter
import com.example.planner.task.Task
import com.example.planner.viewer.AddView


class AddTaskFragment : Fragment(), AddView {
    private lateinit var mListener: FragmentListener
    private lateinit var presenter: ITaskPresenter
    private lateinit var editTaskTitle: TextInputLayout
    private lateinit var editTaskDescription: TextInputLayout
    private var action = TaskActionId.ACTION_ADD.getId()

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_task, container, false)

        init(view)

        return view
    }

    fun setFragmentListener(callback: FragmentListener) {
        mListener = callback
    }

    private fun init(view: View) {
        editTaskTitle = view.findViewById(R.id.taskTitleTextLayout)
        editTaskDescription = view.findViewById(R.id.taskDescriptionTextLayout)

        presenter = TaskPresenter(this, requireContext(), LoaderManager.getInstance(this))

        val bundle = this.arguments
        val task = bundle?.getParcelable<Task>(TaskKey.KEY_TASK.getKey())
        if (task != null) action = TaskActionId.ACTION_EDIT.getId()

        val title =
            if (action == TaskActionId.ACTION_ADD.getId()) getString(R.string.addTaskToolbarTitle)
            else getString(R.string.editTaskToolbarTitle)

        mListener.setupActionBar(title, R.drawable.ic_arrow_back)

        editTaskTitle.editText?.setText(task?.title)
        editTaskTitle.requestFocus()
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
        presenter.updateFields(activity!!.applicationContext, LoaderManager.getInstance(this))
        presenter.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onStop()
        mListener.setupActionBar(getString(R.string.mainToolbarTitle))
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.saveTaskButton) {
            val title = editTaskTitle.editText?.text.toString()
            val desc = editTaskDescription.editText?.text.toString()

            val bundle = this.arguments
            val fav = bundle?.getBoolean(TaskKey.KEY_TASK_FAV.getKey()) ?: false

            if (title.isBlank()) {
                editTaskTitle.error = getString(R.string.addTaskErrorEmpty)
                return true
            }

            when (action) {
                TaskActionId.ACTION_ADD.getId() -> {
                    val task = Task(title, desc, favorite = fav)
                    presenter.updateTask(TaskActionId.ACTION_ADD.getId(), task)
                }
                TaskActionId.ACTION_EDIT.getId() -> {
                    val task = bundle?.getParcelable<Task>(TaskKey.KEY_TASK.getKey())
                    val newTask = Task(title, desc, task!!.id, task.favorite, task.done)
                    presenter.updateTask(TaskActionId.ACTION_EDIT.getId(), newTask)
                }
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onTaskSaveSuccess() {
        hideKeyboard()
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun hideKeyboard() {
        val inputManager = requireActivity()
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        val currentFocusedView = requireActivity().currentFocus
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}
