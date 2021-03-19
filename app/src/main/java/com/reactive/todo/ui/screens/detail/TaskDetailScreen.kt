package com.reactive.todo.ui.screens.detail

import androidx.core.widget.addTextChangedListener
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment
import com.reactive.todo.R
import com.reactive.todo.base.BaseFragment
import com.reactive.todo.data.db.TodoRecord
import com.reactive.todo.ui.adapters.StatusAdapter
import com.reactive.todo.ui.adapters.StatusData
import com.reactive.todo.ui.screens.todolist.STATUS
import com.reactive.todo.ui.screens.todolist.TodoViewModel
import com.reactive.todo.utils.extensions.blockClickable
import com.reactive.todo.utils.extensions.disable
import com.reactive.todo.utils.extensions.enable
import com.reactive.todo.utils.extensions.loge
import kotlinx.android.synthetic.main.content_title.*
import kotlinx.android.synthetic.main.screen_task_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class TaskDetailScreen : BaseFragment<TodoViewModel>(R.layout.screen_task_detail) {

    companion object {
        fun newInstance(data: TodoRecord) = TaskDetailScreen().apply {
            this.data = data
        }
    }

    private val viewModel: TodoViewModel by viewModel()

    override fun getBaseViewModel(): TodoViewModel = viewModel

    private var data: TodoRecord? = null
    private val sdf = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())
    private lateinit var statusAdapter: StatusAdapter
    override fun initialize() {

        initData()

        initViews()

        initStatuses()
    }

    private var statuses = arrayListOf<StatusData>()
    private fun initData() {
        if (data != null) {
            name.setText(data!!.title)
            content.setText(data!!.content)
            date.setText(data!!.date)
            title.text = getString(R.string.task_details)
            add.text = getString(R.string.update_task)
        } else {
            title.text = getString(R.string.add_task_details)
            add.text = getString(R.string.add_task)
        }

    }

    private fun initStatuses() {
        statuses = arrayListOf(StatusData(STATUS.ALL), StatusData(STATUS.NEW), StatusData(STATUS.IN_PROGRESS), StatusData(STATUS.DONE))


        if (data != null) {
            statuses.forEach {
                if (it.name == data!!.status) it.isChecked = true
            }
        } else statuses.first().isChecked = true

        statusAdapter = StatusAdapter {
            statuses.forEach { it.isChecked = false }
            statuses[it].isChecked = true
            statusAdapter.setData(statuses)
        }.apply { setData(statuses) }

        recycler.adapter = statusAdapter
    }

    private fun initViews() {
        add.addTextChangedListener { validateFields() }
        content.addTextChangedListener { validateFields() }
        date.addTextChangedListener { validateFields() }

        add.setOnClickListener {
            it.blockClickable()
            saveUpdateTodo(data == null)
        }

        date.setOnClickListener {
            it.blockClickable()
            openDatePicker()
        }
    }

    /**
     * Validation of EditText
     * */
    private fun validateFields() {
        add.disable()

        if (name.text.isEmpty()) {
            name.requestFocus()
            return
        }

        if (content.text.isEmpty()) {
            content.requestFocus()
            return
        }

        if (date.text.isEmpty()) {
            date.requestFocus()
            return
        }

        add.enable()
    }

    private fun openDatePicker() {
        val dateTimeDialogFragment = SwitchDateTimeDialogFragment.newInstance(
                getString(R.string.enter_date),
                getString(R.string.ok),
                getString(R.string.cancel)
        )

        // Assign values
        dateTimeDialogFragment.apply {
            startAtCalendarView()
            set24HoursMode(true)
            minimumDateTime = Date()
            setDefaultDateTime(Date())
        }

        // Define new day and month format
        try {
            dateTimeDialogFragment.simpleDateMonthAndDayFormat = sdf
        } catch (e: Exception) {
            loge(e.localizedMessage)
            e.printStackTrace()
        }

        // Set listener
        dateTimeDialogFragment.setOnButtonClickListener(object : SwitchDateTimeDialogFragment.OnButtonClickListener {
            override fun onPositiveButtonClick(d: Date?) {
                d?.let { date.setText(sdf.format(d)) }
            }

            override fun onNegativeButtonClick(d: Date?) {
            }

        })

        // show
        dateTimeDialogFragment.show(childFragmentManager, "dialog_time")
    }

    /**
     * Saves the new / updated information and back to calling fragment
     * */
    private fun saveUpdateTodo(save: Boolean) {
        val todo = TodoRecord(id = data?.id, title = name.text.toString(), content = content.text.toString(), date = date.text.toString(), status = statuses.first { it.isChecked }.name)
        if (save) viewModel.saveTodo(todo)
        else viewModel.updateTodo(todo)
        finishFragment()
    }

}