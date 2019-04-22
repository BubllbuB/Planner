package com.example.planner.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.example.planner.FRAGMENT_TAG_ADDTASK
import com.example.planner.R
import com.example.planner.enums.TaskAction
import com.example.planner.enums.TaskKey
import com.example.planner.observer.FragmentListener
import com.example.planner.presenters.TaskPresenter
import com.example.planner.task.Task
import com.example.planner.viewer.AddView
import kotlinx.android.synthetic.main.fragment_add_task.*

const val FRAME_RECREATE = "frameRecreate"

class AddTaskFragment : MvpAppCompatFragment(), AddView {
    private lateinit var mListener: FragmentListener
    @InjectPresenter
    lateinit var presenter: TaskPresenter
    private var action = TaskAction.ACTION_ADD

    private val titleTextWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            taskTitleTextLayout.error = null
        }

        override fun onTextChanged(title: CharSequence?, p1: Int, p2: Int, p3: Int) {
            presenter.onSetTitle(title.toString(), p1, p3)
        }
    }

    private val descriptionTextWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(title: CharSequence?, p1: Int, p2: Int, p3: Int) {
            presenter.onSetDescription(title.toString(), p1, p3)
        }
    }

    @ProvidePresenter
    fun provideTaskPresenter() = TaskPresenter(requireContext(), LoaderManager.getInstance(this))

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(savedInstanceState)
    }

    override fun setTitle(title: String, startPos: Int, endPos: Int) {
        taskTitleTextLayout.editText?.removeTextChangedListener(titleTextWatcher)
        taskTitleTextLayout.editText?.setText(title)
        taskTitleTextLayout.editText?.setSelection(startPos + endPos)
        taskTitleTextLayout.editText?.addTextChangedListener(titleTextWatcher)
    }

    override fun setDescription(title: String, startPos: Int, endPos: Int) {
        taskDescriptionTextLayout.editText?.removeTextChangedListener(descriptionTextWatcher)
        taskDescriptionTextLayout.editText?.setText(title)
        taskDescriptionTextLayout.editText?.setSelection(startPos + endPos)
        taskDescriptionTextLayout.editText?.addTextChangedListener(descriptionTextWatcher)
    }

    override fun setFocus(focusId: Int) {
        when(focusId) {
            1 -> taskTitleTextLayout.requestFocus()
            2 -> taskDescriptionTextLayout.requestFocus()
        }
    }

    fun setFragmentListener(callback: FragmentListener) {
        mListener = callback
    }

    private fun init(savedInstanceState: Bundle?) {
        val bundle = this.arguments
        val task = bundle?.getParcelable<Task>(TaskKey.KEY_TASK.getKey())
        if (task != null) action = TaskAction.ACTION_EDIT

        val title =
            if (action == TaskAction.ACTION_ADD) getString(R.string.addTaskToolbarTitle)
            else getString(R.string.editTaskToolbarTitle)

        taskTitleTextLayout.editText?.setText(task?.title)

        taskDescriptionTextLayout.editText?.setText(task?.description)

        taskTitleTextLayout.editText?.setOnClickListener {
            taskTitleTextLayout.error = null
        }

        taskTitleTextLayout.editText?.addTextChangedListener(titleTextWatcher)
        taskDescriptionTextLayout.editText?.addTextChangedListener(descriptionTextWatcher)

        taskTitleTextLayout.editText?.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus) {
                presenter.onSetFocus(1)
            }
        }

        taskDescriptionTextLayout.editText?.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus) {
                presenter.onSetFocus(2)
            }
        }

        val isRecreated = bundle?.getBoolean(FRAME_RECREATE) ?: false

        if (!isRecreated && savedInstanceState != null) {
            val oldState = requireActivity().supportFragmentManager.saveFragmentInstanceState(this)
            val dupFragment = AddTaskFragment()
            dupFragment.setInitialSavedState(oldState)
            val newBundle = Bundle()
            newBundle.putParcelable(TaskKey.KEY_TASK.getKey(), task)
            newBundle.putBoolean(FRAME_RECREATE, true)
            dupFragment.arguments = newBundle

            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()

            if (requireContext().resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.content_fragments, dupFragment, FRAGMENT_TAG_ADDTASK)
                    .addToBackStack(null)
                    .commit()
            } else {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.edit_fragment, dupFragment)
                    .commit()
            }
        } else if(isRecreated) {
            bundle?.putBoolean(FRAME_RECREATE, false)
        }

        mListener.setupActionBar(title, R.drawable.ic_arrow_back)
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
        if (requireContext().resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) {
            activity?.supportFragmentManager?.popBackStack()
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
