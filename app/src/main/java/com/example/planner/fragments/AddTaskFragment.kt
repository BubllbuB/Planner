package com.example.planner.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import com.example.planner.R
import com.example.planner.enums.TaskAction
import com.example.planner.enums.TaskKey
import com.example.planner.observer.FragmentListener
import com.example.planner.presenters.ITaskPresenter
import com.example.planner.presenters.TaskPresenter
import com.example.planner.task.Task
import com.example.planner.viewer.AddView
import kotlinx.android.synthetic.main.fragment_add_task.*


class AddTaskFragment : Fragment(), AddView {
    private lateinit var mListener: FragmentListener
    private lateinit var presenter: ITaskPresenter
    private var action = TaskAction.ACTION_ADD

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    fun setFragmentListener(callback: FragmentListener) {
        mListener = callback
    }

    private fun init() {
        presenter = TaskPresenter(this, requireContext(), LoaderManager.getInstance(this))

        val bundle = this.arguments
        val task = bundle?.getParcelable<Task>(TaskKey.KEY_TASK.getKey())
        if (task != null) action = TaskAction.ACTION_EDIT

        val title =
            if (action == TaskAction.ACTION_ADD) getString(R.string.addTaskToolbarTitle)
            else getString(R.string.editTaskToolbarTitle)

        mListener.setupActionBar(title, R.drawable.ic_arrow_back)

        taskTitleTextLayout.editText?.setText(task?.title)
        taskTitleTextLayout.requestFocus()
        taskDescriptionTextLayout.editText?.setText(task?.description)

        taskTitleTextLayout.editText?.setOnClickListener {
            taskTitleTextLayout.error = null
        }

        taskTitleTextLayout.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                taskTitleTextLayout.error = null
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    override fun onStart() {
        super.onStart()
        presenter.updateFields(requireContext(), LoaderManager.getInstance(this))
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
            val title = taskTitleTextLayout.editText?.text.toString()
            val desc = taskDescriptionTextLayout.editText?.text.toString()

            val bundle = this.arguments
            val fav = bundle?.getBoolean(TaskKey.KEY_TASK_FAV.getKey()) ?: false

            if (title.isBlank()) {
                taskTitleTextLayout.error = getString(R.string.addTaskErrorEmpty)
                return true
            }

            when (action) {
                TaskAction.ACTION_ADD -> {
                    val task = Task(title, desc, favorite = fav)
                    presenter.updateTask(TaskAction.ACTION_ADD, task)
                }
                else -> {
                    val task = bundle?.getParcelable<Task>(TaskKey.KEY_TASK.getKey())
                    val newTask = Task(title, desc, task!!.id, task.favorite, task.done)
                    presenter.updateTask(TaskAction.ACTION_EDIT, newTask)
                }
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onTaskSaveSuccess() {
        hideKeyboard()
        activity.let {
            requireActivity().supportFragmentManager.popBackStack()
        }
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
