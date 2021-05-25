@file:JvmName("SwipeBackUtil")

package androidx.fragment.app

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.fragment.R
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.ViewTreeSavedStateRegistryOwner
import com.github.fragivity.appendBackground
import com.github.fragivity.navigator
import com.github.fragivity.pop
import com.github.fragivity.swipeback.SwipeBackLayout

private fun Fragment.attachToSwipeBack(view: View): View {

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
    var rootView = requireView()
    if (rootView !is SwipeBackLayout) {
        val parent = rootView.parent as ViewGroup
        parent.removeView(rootView)

        rootView = attachToSwipeBack(rootView).also {
            it.setTag(R.id.fragment_container_view_tag, this)
            ViewTreeLifecycleOwner.set(it, viewLifecycleOwner)
            ViewTreeViewModelStoreOwner.set(it, this)
            ViewTreeSavedStateRegistryOwner.set(it, viewLifecycleOwner as SavedStateRegistryOwner)
        }

        parent.addView(rootView)
        this.mView = rootView
    }

    (rootView as SwipeBackLayout).setEnableGesture(enable)

}

/**
 * Global config for enabling swipe back
 */
@JvmField
internal var enableSwipeBack = false