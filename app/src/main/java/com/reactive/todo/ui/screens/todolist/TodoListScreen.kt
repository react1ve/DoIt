package com.reactive.todo.ui.screens.todolist

import android.os.Handler
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.reactive.todo.R
import com.reactive.todo.base.BaseFragment
import com.reactive.todo.data.db.TodoRecord
import com.reactive.todo.ui.adapters.TodoAdapter
import com.reactive.todo.ui.screens.detail.TaskDetailScreen
import com.reactive.todo.utils.extensions.blockClickable
import com.reactive.todo.utils.extensions.gone
import com.reactive.todo.utils.extensions.showGone
import com.reactive.todo.utils.extensions.visible
import kotlinx.android.synthetic.main.content_title.*
import kotlinx.android.synthetic.main.screen_recycler.*
import org.koin.androidx.viewmodel.ext.android.viewModel

enum class STATUS { ALL, DONE, IN_PROGRESS, NEW }

class ListScreen : BaseFragment<TodoViewModel>(R.layout.screen_recycler) {

    private val viewModel: TodoViewModel by viewModel()

    override fun getBaseViewModel(): TodoViewModel {
        return viewModel
    }

    companion object {
        fun newInstance(status: STATUS) = ListScreen().apply {
            this.status = status
        }
    }

    private lateinit var status: STATUS
    private lateinit var adapter: TodoAdapter
    private val handler = Handler()
    private var r = Runnable { }
    override fun initialize() {

        title.text = when (status) {
            STATUS.ALL -> getString(R.string.all_tasks)
            STATUS.DONE -> getString(R.string.completed_tasks)
            STATUS.IN_PROGRESS -> getString(R.string.remaining_tasks)
            STATUS.NEW -> getString(R.string.new_tasks)
        }

        initViews()

        initAdapter()
    }

    // Functionalities of other views
    private fun initViews() {

        search.addTextChangedListener {
            filter(it.toString())
            if (it.toString().isEmpty()) search.clearFocus()
        }

        fab.setOnClickListener {
            it.blockClickable()
            addFragment(TaskDetailScreen())
        }

        fab.showGone(status == STATUS.ALL)

        swipeRefreshLayout.setOnRefreshListener { observe() }

    }

    // Initialize list
    private fun initAdapter() {

        adapter = TodoAdapter { data, isDeleted ->
            if (isDeleted) viewModel.deleteTodo(data)
            else removePreviousCallback({
                addFragment(TaskDetailScreen.newInstance(data))
            })
        }
        recycler.adapter = adapter

        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recycler, dx, dy)
                if (dy > 0 && fab.visibility == View.VISIBLE) {
                    fab.hide()
                } else if (dy < 0 && fab.visibility != View.VISIBLE && status == STATUS.ALL) {
                    fab.show()
                }
            }
        })
    }

    private fun filter(query: String) {
        adapter.filter.filter(query)
    }

    //Setting up LiveData & load tasks
    override fun observe() {
        shimmerLayout.startShimmerAnimation()
        shimmerLayout.visible()

        viewModel.getAllTodoList().observe(this, Observer {
            setData(it)
        })
    }

    private fun setData(data: List<TodoRecord>) {
        r = Runnable {
            swipeRefreshLayout?.isRefreshing = false
            shimmerLayout?.stopShimmerAnimation()
            shimmerLayout?.gone()
            when (status) {
                STATUS.ALL -> adapter.setData(data)
                STATUS.DONE -> adapter.setData(data.filter { it.status == STATUS.DONE })
                STATUS.IN_PROGRESS -> adapter.setData(data.filter { it.status == STATUS.IN_PROGRESS })
                STATUS.NEW -> adapter.setData(data.filter { it.status == STATUS.NEW })
            }
        }
        handler.postDelayed(r, 400)
    }

    override fun onDestroyView() {
        handler.removeCallbacks(r)
        super.onDestroyView()
    }
}