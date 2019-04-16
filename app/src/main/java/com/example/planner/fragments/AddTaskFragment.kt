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
import com.example.planner.R
import com.example.planner.enums.TaskAction
import com.example.planner.enums.TaskKey
import com.example.planner.observer.FragmentListener
import com.example.planner.presenters.TaskPresenter
import com.example.planner.task.Task
import com.example.planner.viewer.AddView
import kotlinx.android.synthetic.main.fragment_add_task.*


const val TEXT_LAYOUT_TITLE_TEXT = "titleTextLayoutText"
const val TEXT_LAYOUT_TITLE_FOCUS = "titleTextLayoutFocus"
const val TEXT_LAYOUT_TITLE_START_POS = "titleTextLayoutStartPos"
const val TEXT_LAYOUT_TITLE_FINAL_POS = "titleTextLayoutFinalPos"
const val TEXT_LAYOUT_DESCRIPTION_TEXT = "descriptionTextLayoutText"
const val TEXT_LAYOUT_DESCRIPTION_FOCUS = "descriptionTextLayoutFocus"
const val TEXT_LAYOUT_DESCRIPTION_START_POS = "descriptionTextLayoutStartPos"
const val TEXT_LAYOUT_DESCRIPTION_FINAL_POS = "descriptionTextLayoutFinalPos"
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

        if (savedInstanceState != null) {

            taskTitleTextLayout.editText?.setText(savedInstanceState.getString(TEXT_LAYOUT_TITLE_TEXT, ""))
            taskDescriptionTextLayout.editText?.setText(savedInstanceState.getString(TEXT_LAYOUT_DESCRIPTION_TEXT, ""))
            taskTitleTextLayout.editText?.setSelection(
                savedInstanceState.getInt(TEXT_LAYOUT_TITLE_START_POS),
                savedInstanceState.getInt(TEXT_LAYOUT_TITLE_FINAL_POS)
            )
            taskDescriptionTextLayout.editText?.setSelection(
                savedInstanceState.getInt(TEXT_LAYOUT_DESCRIPTION_START_POS),
                savedInstanceState.getInt(TEXT_LAYOUT_DESCRIPTION_FINAL_POS)
            )

            if (savedInstanceState.getBoolean(TEXT_LAYOUT_TITLE_FOCUS)) {
                taskTitleTextLayout.requestFocus()
            } else if (savedInstanceState.getBoolean(TEXT_LAYOUT_DESCRIPTION_FOCUS)) {
                taskDescriptionTextLayout.requestFocus()
            }

            val isRecreated = bundle?.getBoolean(FRAME_RECREATE) ?: false

            if (requireContext().resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) {
                if (savedInstanceState.getBoolean(TEXT_LAYOUT_TITLE_FOCUS)
                    || savedInstanceState.getBoolean(TEXT_LAYOUT_DESCRIPTION_FOCUS)
                    && !isRecreated
                ) {

                    val dupFragment = AddTaskFragment()
                    val oldState = requireActivity().supportFragmentManager.saveFragmentInstanceState(this)
                    dupFragment.setInitialSavedState(oldState)
                    val newBundle = Bundle()
                    newBundle.putParcelable(TaskKey.KEY_TASK.getKey(),task)
                    newBundle.putBoolean(FRAME_RECREATE, true)
                    dupFragment.arguments = newBundle

                    requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()

                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.content_fragments, dupFragment)
                        .addToBackStack(null)
                        .commit()
                }

            }
            mListener.setupActionBar(title, R.drawable.ic_arrow_back)
        } else {
            taskTitleTextLayout.requestFocus()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TEXT_LAYOUT_TITLE_TEXT, taskTitleTextLayout.editText?.text.toString())
        outState.putBoolean(TEXT_LAYOUT_TITLE_FOCUS, taskTitleTextLayout.editText!!.hasFocus())
        outState.putInt(TEXT_LAYOUT_TITLE_START_POS, taskTitleTextLayout.editText!!.selectionStart)
        outState.putInt(TEXT_LAYOUT_TITLE_FINAL_POS, taskTitleTextLayout.editText!!.selectionEnd)

        outState.putString(TEXT_LAYOUT_DESCRIPTION_TEXT, taskDescriptionTextLayout.editText?.text.toString())
        outState.putBoolean(TEXT_LAYOUT_DESCRIPTION_FOCUS, taskDescriptionTextLayout.editText!!.hasFocus())
        outState.putInt(TEXT_LAYOUT_DESCRIPTION_START_POS, taskDescriptionTextLayout.editText!!.selectionStart)
        outState.putInt(TEXT_LAYOUT_DESCRIPTION_FINAL_POS, taskDescriptionTextLayout.editText!!.selectionEnd)
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
