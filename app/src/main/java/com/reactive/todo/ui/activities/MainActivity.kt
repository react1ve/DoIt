package com.reactive.todo.ui.activities

import android.view.KeyEvent
import androidx.annotation.IdRes
import com.reactive.todo.R
import com.reactive.todo.base.BaseActivity
import com.reactive.todo.base.initialFragment
import com.reactive.todo.ui.screens.BottomNavScreen

class MainActivity : BaseActivity(R.layout.activity_main) {

    companion object {
        @IdRes
        var parentLayoutId: Int = 0

        @IdRes
        var navLayoutId: Int = 0
    }

    override fun onActivityCreated() {
        init()
    }

    private fun init() {

        parentLayoutId = R.id.fragmentContainer
        navLayoutId = R.id.navContainer

        startFragment()
    }

    private fun startFragment() {
        initialFragment(BottomNavScreen(), parentLayoutId)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            when (event.keyCode) {
                KeyEvent.KEYCODE_VOLUME_UP -> return false
                KeyEvent.KEYCODE_VOLUME_DOWN -> return false
            }
        }
        return super.dispatchKeyEvent(event)
    }
}
