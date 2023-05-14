package androidx.fragment.app

import android.view.View
import androidx.fragment.R
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner

internal fun Fragment.setView(view: View) {
    view.setTag(R.id.fragment_container_view_tag, this)
    ViewTreeLifecycleOwner.set(view, viewLifecycleOwner)
    ViewTreeViewModelStoreOwner.set(view, this)
    view.setViewTreeSavedStateRegistryOwner(viewLifecycleOwner as SavedStateRegistryOwner)
    mView = view
}