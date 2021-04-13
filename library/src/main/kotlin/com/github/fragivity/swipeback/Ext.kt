@file:JvmName("SwipeBackUtil")

package com.github.fragivity.swipeback

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.github.fragivity.navigator
import com.github.fragivity.pop
import com.github.fragivity.appendBackground

fun Fragment.attachToSwipeBack(view: View): View {
    val swipeBackLayout = SwipeBackLayout(requireContext()).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        setBackgroundColor(Color.TRANSPARENT)
        attachToFragment(this@attachToSwipeBack, view)
        addSwipeListener(object : SwipeBackLayout.SimpleOnSwipeListener() {
            override fun onDragFinished(isActivity: Boolean) {
                if (!isActivity) {
                    navigator.pop()
                }
            }
        })
    }

    view.appendBackground()

    lifecycle.addObserver(LifecycleEventObserver { _, event ->
        if (Lifecycle.Event.ON_DESTROY == event) {
            swipeBackLayout.internalCallOnDestroyView()
        }
    })
    return swipeBackLayout
}

fun Fragment.setEnableGesture(enable: Boolean) {
    val rootView = requireView()
    if (rootView is SwipeBackLayout) {
        rootView.setEnableGesture(enable)
    }
}

/**
 * Global config for enabling swipe back
 */
@JvmField
internal var enableSwipeBack = false