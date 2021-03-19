package com.reactive.todo.ui.activities

import android.annotation.SuppressLint
import android.view.KeyEvent
import androidx.annotation.IdRes
import com.reactive.todo.R
import com.reactive.todo.base.BaseActivity
import com.reactive.todo.base.initialFragment
import com.reactive.todo.ui.screens.BottomNavScreen
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : BaseActivity(R.layout.activity_main) {

    companion object {
        @IdRes
        var parentLayoutId: Int = 0

        @IdRes
        var navLayoutId: Int = 0
    }

    override fun onActivityCreated() {

        init()

        setDate()
    }

    private fun init() {

        parentLayoutId = R.id.fragmentContainer
        navLayoutId = R.id.navContainer

        startFragment()
    }

    @SuppressLint("SimpleDateFormat")
    private fun setDate() {
        date.text = SimpleDateFormat("dd MMMM, yyyy").format(Date())
        week.text = SimpleDateFormat("EEEE").format(Date())
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
