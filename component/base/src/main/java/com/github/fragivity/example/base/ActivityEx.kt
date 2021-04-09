package com.github.fragivity.example.base

import android.app.Activity
import android.widget.Toast
import androidx.annotation.StringRes

fun Activity.showToast(@StringRes resId: Int) {
    showToast(getString(resId))
}

fun Activity.showToast(msg: String?) {
    msg ?: return
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
