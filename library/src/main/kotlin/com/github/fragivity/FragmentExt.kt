@file:JvmName("FragivityUtil")
@file:JvmMultifileClass

package com.github.fragivity

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import kotlin.reflect.KClass

fun Fragment.findNavHostFragment(@IdRes id: Int): NavHostFragment {
    return childFragmentManager.findFragmentById(id) as NavHostFragment
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