package androidx.navigation

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import com.github.fragivity.R
import kotlin.math.abs

/**
 * @author wangpeng.rocky@bytedance.com
 */

fun NavHostFragment.showDebugView(root: ViewGroup) {
    val context = root.context
    val stackView = ImageView(context)
    stackView.setImageResource(R.drawable.fragmentation_ic_stack)
    val dp18 = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        18f,
        context.resources.displayMetrics
    ).toInt()
    val params = FrameLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply {
        gravity = Gravity.END
        topMargin = dp18 * 7
        rightMargin = dp18
    }
    stackView.layoutParams = params
    root.addView(stackView)
    stackView.setOnTouchListener(
        StackViewTouchListener(
            stackView,
            dp18 / 4
        )
    )
    stackView.setOnClickListener { showFragmentStackHierarchyView(context) }
}

private var mStackDialog: AlertDialog? = null

internal fun NavHostFragment.showFragmentStackHierarchyView(context: Context) {
    if (mStackDialog != null && mStackDialog!!.isShowing) return

    val tv = TextView(context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        var res = "NavGraph "
        navController.mBackStack.forEach {
            it.destination.let { des ->
                if (des is FragmentNavigator.Destination) {
                    res += "\n +- ${des.className}:${des.id}"
                }
            }
        }
        text = res
        gravity = Gravity.CENTER_VERTICAL
    }

    mStackDialog = AlertDialog.Builder(context)
        .setView(tv)
        .setPositiveButton(android.R.string.cancel, null)
        .setCancelable(true)
        .create()
    mStackDialog!!.show()
}

private class StackViewTouchListener(
    private val stackView: View,
    private val clickLimitValue: Int
) :
    OnTouchListener {
    private var dX = 0f
    private var dY = 0f
    private var downX = 0f
    private var downY = 0f
    private var isClickState = false
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val X = event.rawX
        val Y = event.rawY
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isClickState = true
                downX = X
                downY = Y
                dX = stackView.x - event.rawX
                dY = stackView.y - event.rawY
            }
            MotionEvent.ACTION_MOVE -> if (abs(X - downX) < clickLimitValue && Math.abs(Y - downY) < clickLimitValue && isClickState) {
                isClickState = true
            } else {
                isClickState = false
                stackView.x = event.rawX + dX
                stackView.y = event.rawY + dY
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> if (X - downX < clickLimitValue && isClickState) {
                stackView.performClick()
            }
            else -> return false
        }
        return true
    }
}