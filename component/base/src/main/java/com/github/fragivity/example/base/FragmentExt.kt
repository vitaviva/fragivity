package com.github.fragivity.example.base

import android.os.Build
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.fragivity.navigator
import com.github.fragivity.pop

fun Fragment.initToolbarNav(toolbar: Toolbar) {
    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
    toolbar.setNavigationOnClickListener { navigator.pop() }
}

fun Fragment.showToast(@StringRes resId: Int) {
    showToast(getString(resId))
}

fun Fragment.showToast(msg: String?) {
    msg ?: return
    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
}

fun Fragment.hideSoftInput() {
    val decorView = requireActivity().window.decorView
    val imm = ContextCompat.getSystemService(requireContext(), InputMethodManager::class.java)
    imm?.hideSoftInputFromWindow(decorView.windowToken, 0)
}

fun Fragment.doOnIdle(block: () -> Unit) {
    requireView().doOnIdle(Runnable(block))
}

fun View.doOnIdle(runnable: Runnable) {
    when {
        Looper.myLooper() == Looper.getMainLooper() -> {
            Looper.myQueue().addIdleHandler {
                runnable.run()
                false
            }
        }
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
            Looper.getMainLooper().queue.addIdleHandler {
                runnable.run()
                false
            }
        }
        else -> {
            post {
                Looper.myQueue().addIdleHandler {
                    runnable.run()
                    false
                }
            }
        }
    }
}