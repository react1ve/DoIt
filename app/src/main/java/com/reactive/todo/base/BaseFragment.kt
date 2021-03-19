package com.reactive.todo.base

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.reactive.todo.R
import com.reactive.todo.ui.activities.MainActivity

abstract class BaseFragment<T : BaseViewModel>(@LayoutRes val layoutId: Int) : Fragment(layoutId) {

    protected var enableCustomBackPress = false
    private var viewModel: T? = null

    abstract fun getBaseViewModel(): T?

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = getBaseViewModel()

        retainInstance = false

        initialize()

        setFocus(view)

        observe()
    }

    abstract fun initialize()

    protected fun navLayoutId() = MainActivity.navLayoutId

    protected fun parentLayoutId() = MainActivity.parentLayoutId

    fun addFragment(
            fragment: Fragment,
            addBackStack: Boolean = true, @IdRes id: Int = parentLayoutId(),
            tag: String = fragment.javaClass.canonicalName?.toString() ?: fragment.hashCode()
                    .toString()
    ) {
        hideKeyboard()
        activity?.supportFragmentManager?.commit(allowStateLoss = true) {
            if (addBackStack && !fragment.isAdded) addToBackStack(tag)
            setCustomAnimations(
                    R.anim.enter_from_bottom,
                    R.anim.exit_to_top,
                    R.anim.enter_from_top,
                    R.anim.exit_to_bottom
            )
            add(id, fragment)
        }
    }

    fun replaceFragment(fragment: Fragment, @IdRes id: Int = navLayoutId()) {
        hideKeyboard()
        activity?.supportFragmentManager?.commit(allowStateLoss = true) {
            replace(id, fragment)
        }
    }

    fun finishFragment() {
        hideKeyboard()
        activity?.supportFragmentManager?.popBackStackImmediate()
    }

    fun popInclusive(name: String? = null, flags: Int = FragmentManager.POP_BACK_STACK_INCLUSIVE) {
        hideKeyboard()
        activity?.supportFragmentManager?.popBackStackImmediate(name, flags)
    }

    protected open fun onFragmentBackButtonPressed() {
    }

    protected open fun observe() {
    }

    fun hideKeyboard() {
        view?.let {
            val imm =
                    it.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    private fun setFocus(view: View) {
        view.apply {
            isFocusableInTouchMode = true
            setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (enableCustomBackPress) onFragmentBackButtonPressed()
                    else activity?.onBackPressed()
                }
                enableCustomBackPress = false
                true
            }
        }
    }

    fun windowAdjustPan() =
            activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

    fun windowAdjustResize() =
            activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

    private val baseHandler = Handler()
    private var baseRunnable = Runnable {
    }

    fun removePreviousCallback(action: () -> Unit, millis: Long = 500) {
        baseHandler.removeCallbacks(baseRunnable)
        baseRunnable = Runnable { action() }
        baseHandler.postDelayed(baseRunnable, millis)
    }
}

fun FragmentActivity.initialFragment(fragment: Fragment, containerId: Int) {
    supportFragmentManager.commit(allowStateLoss = true) {
        setCustomAnimations(
                R.anim.enter_from_bottom,
                R.anim.exit_to_top,
                R.anim.enter_from_top,
                R.anim.exit_to_bottom)
        replace(containerId, fragment)
    }
}

fun FragmentActivity.finishFragment() {
    supportFragmentManager.popBackStack()
}