package androidx.fragment.app

import android.view.View
import androidx.fragment.R
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.ViewTreeSavedStateRegistryOwner

@JvmSynthetic
internal fun Fragment.setView(view: View) {
    view.setTag(R.id.fragment_container_view_tag, this)
    ViewTreeLifecycleOwner.set(view, viewLifecycleOwner)
    ViewTreeViewModelStoreOwner.set(view, this)
    ViewTreeSavedStateRegistryOwner.set(view, viewLifecycleOwner as SavedStateRegistryOwner)
    mView = view
}

@JvmSynthetic
internal fun setFragmentState(fragmentManager: FragmentManager, fragment: Fragment, state: Int) {
    fragment.mState = state
    fragmentManager.getFragmentStateManager(fragment)?.setFragmentManagerState(state)
}

@JvmSynthetic
internal fun moveFragmentState(fragmentManager: FragmentManager, fragment: Fragment, state: Int) {
    fragmentManager.getFragmentStateManager(fragment)?.let {
        it.setFragmentManagerState(state)
        it.moveToExpectedState()
    }
}

private fun FragmentManager.getFragmentStateManager(fragment: Fragment): FragmentStateManager? {
    return fragmentStore.getFragmentStateManager(fragment.mWho)
}