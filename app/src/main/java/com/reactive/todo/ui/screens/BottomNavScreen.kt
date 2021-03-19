package com.reactive.todo.ui.screens

import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.reactive.todo.R
import com.reactive.todo.base.BaseFragment
import com.reactive.todo.ui.screens.todolist.ListScreen
import com.reactive.todo.ui.screens.todolist.STATUS
import kotlinx.android.synthetic.main.screen_bottom_nav.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class BottomNavScreen : BaseFragment<BottomNavViewModel>(R.layout.screen_bottom_nav) {

    private val viewModel by viewModel<BottomNavViewModel>()

    override fun getBaseViewModel(): BottomNavViewModel {
        return viewModel
    }

    private var bottomFragments = arrayListOf<Fragment>(
            ListScreen.newInstance(STATUS.ALL),
            ListScreen.newInstance(STATUS.DONE),
            ListScreen.newInstance(STATUS.IN_PROGRESS)
    )

    override fun initialize() {

        setHasOptionsMenu(true)

        bottomNav.setOnNavigationItemSelectedListener { item: MenuItem ->
            return@setOnNavigationItemSelectedListener when (item.itemId) {
                R.id.all -> {
                    selectFragment(0)
                    true
                }
                R.id.done -> {
                    selectFragment(1)
                    true
                }
                R.id.progress -> {
                    selectFragment(2)
                    true
                }
                else -> false
            }
        }

        selectFragment(0)
    }

    private fun selectFragment(pos: Int) {
        replaceFragment(bottomFragments[pos], R.id.navContainer)
    }
}
