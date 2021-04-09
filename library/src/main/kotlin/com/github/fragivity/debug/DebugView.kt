package com.github.fragivity.debug

import android.annotation.SuppressLint
import android.app.Activity
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.navigation.fragment.NavHostFragment
import com.github.fragivity.R
import com.github.fragivity.util.dp
import kotlin.math.abs

private const val DEFAULT_DEBUG_DIALOG_TAG = "DebugViewDialogFragment"

fun NavHostFragment.showDebugView(activity: Activity, tag: String = DEFAULT_DEBUG_DIALOG_TAG) {
    showDebugView(activity.findViewById<FrameLayout>(android.R.id.content), tag)
}

@SuppressLint("ClickableViewAccessibility")
fun NavHostFragment.showDebugView(root: ViewGroup, tag: String = DEFAULT_DEBUG_DIALOG_TAG) {
    val context = root.context

    val dp18 = 18f.dp(context)
    val stackView = ImageView(context).apply {
        setImageResource(R.drawable.ic_stack)
        layoutParams = FrameLayout.LayoutParams(
            dp18 * 3,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.END or Gravity.BOTTOM
            rightMargin = dp18
            bottomMargin = dp18 * 3
        }
    }
    root.addView(stackView)

    stackView.setOnTouchListener(StackViewTouchListener(stackView, dp18 / 4))
    stackView.setOnClickListener {
        DebugViewDialogFragment().show(childFragmentManager, tag)
    }
}

private class StackViewTouchListener(
    private val stackView: View,
    private val clickLimitValue: Int
) : OnTouchListener {
    private var dX = 0f
    private var dY = 0f
    private var downX = 0f
    private var downY = 0f
    private var isClickState = false
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val x = event.rawX
        val y = event.rawY
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isClickState = true
                downX = x
                downY = y
                dX = stackView.x - event.rawX
                dY = stackView.y - event.rawY
            }
            MotionEvent.ACTION_MOVE -> if (
                abs(x - downX) < clickLimitValue &&
                abs(y - downY) < clickLimitValue &&
                isClickState
            ) {
                isClickState = true
            } else {
                isClickState = false
                stackView.x = event.rawX + dX
                stackView.y = event.rawY + dY
            }
            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_UP -> if (
                x - downX < clickLimitValue &&
                isClickState
            ) {
                stackView.performClick()
            }
            else -> return false
        }
        return true
    }
}