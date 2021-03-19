package com.reactive.todo.ui.screens.detail

import androidx.core.widget.addTextChangedListener
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment
import com.reactive.todo.R
import com.reactive.todo.base.BaseFragment
import com.reactive.todo.data.db.TodoRecord
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
    override fun initialize() {

        initData()

        initViews()
    }

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

    private fun initViews() {
        add.addTextChangedListener { validateFields() }
        content.addTextChangedListener { validateFields() }
        date.addTextChangedListener { validateFields() }

        add.setOnClickListener {
            it.blockClickable()
            if (data != null) updateTodo()
            else saveTodo()
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

        if (title.text.isEmpty()) {
            title.requestFocus()
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
     * Saves the new information back to calling fragment
     * */
    private fun saveTodo() {
        val todo = TodoRecord(id = data?.id, title = title.text.toString(), content = content.text.toString(), date = date.text.toString(), status = STATUS.NEW)
        viewModel.saveTodo(todo)
        finishFragment()
    }

    /**
     * Saves the updated information and back to calling fragment
     * */
    private fun updateTodo() {
        val todo = TodoRecord(id = data!!.id, title = title.text.toString(), content = content.text.toString(), date = date.text.toString(), status = data!!.status)
        viewModel.updateTodo(todo)
        finishFragment()
    }

}