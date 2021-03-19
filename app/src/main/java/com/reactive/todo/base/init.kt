package com.reactive.todo.base

import android.os.Handler
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.reactive.todo.R

var exit = false
fun FragmentActivity.exitVariant() {
    if (exit) {
        finishAffinity()
    } else {
        Toast.makeText(this, this.getString(R.string.back_again), Toast.LENGTH_SHORT).show()
        exit = true
        Handler().postDelayed({ exit = false }, 2000)
    }
}
