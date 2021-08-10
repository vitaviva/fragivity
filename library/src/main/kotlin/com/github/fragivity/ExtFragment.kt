@file:JvmName("FragivityUtil")
@file:JvmMultifileClass

package com.github.fragivity

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.fragment.NavHostFragment
import kotlin.reflect.KClass

fun Fragment.fragmentContainerView(@IdRes id: Int): FragmentContainerView {
    return FragmentContainerView(requireContext()).apply { this.id = id }
}

fun Fragment.findNavHostFragment(@IdRes id: Int): NavHostFragment? {
    return childFragmentManager.findFragmentById(id) as? NavHostFragment
}

fun Fragment.findOrCreateNavHostFragment(
    @IdRes id: Int,
    isReport: Boolean = true
): NavHostFragment {
    var navHostFragment = findNavHostFragment(id)
    if (navHostFragment == null) {
        navHostFragment = childFragmentManager.createNavHostFragment(id, isReport)
    }
    return navHostFragment
}

fun <T : Fragment> Fragment.findFragment(
    clazz: KClass<T>,
    includeChild: Boolean = true
): T? {
    return findFragment(childFragmentManager, clazz, includeChild)
}

/**
 * finish Activity
 */
fun Fragment.finish() {
    requireActivity().finish()
}