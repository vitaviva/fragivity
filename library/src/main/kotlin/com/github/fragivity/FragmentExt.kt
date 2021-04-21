@file:JvmName("FragivityUtil")
@file:JvmMultifileClass

package com.github.fragivity

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import kotlin.reflect.KClass

val Fragment.navigator: FragivityNavHost
    get() = fragivityHostViewModel.navHost

val View.navigator: FragivityNavHost
    get() = fragivityHostViewModel.navHost

val NavController.navigator: FragivityNavHost
    get() = fragivityHostViewModel.navHost

fun FragmentActivity.findNavHostFragment(@IdRes id: Int): NavHostFragment {
    return supportFragmentManager.findFragmentById(id) as NavHostFragment
}

fun Fragment.findNavHostFragment(@IdRes id: Int): NavHostFragment {
    return childFragmentManager.findFragmentById(id) as NavHostFragment
}

fun <T : Fragment> FragmentActivity.findFragment(
    clazz: KClass<T>,
    includeChild: Boolean = true
): T? {
    return findFragment(supportFragmentManager, clazz, includeChild)
}

fun <T : Fragment> Fragment.findFragment(
    clazz: KClass<T>,
    includeChild: Boolean = true
): T? {
    return findFragment(childFragmentManager, clazz, includeChild)
}

private fun <T : Fragment> findFragment(
    manager: FragmentManager,
    clazz: KClass<T>,
    includeChild: Boolean
): T? {
    val fragments = manager.fragments
    if (fragments.isEmpty()) return null

    fragments.forEach { fragment ->
        if (fragment.javaClass.name == clazz.java.name) {
            @Suppress("UNCHECKED_CAST")
            return fragment as T
        }
        if (includeChild) {
            val childFragment = findFragment(fragment.childFragmentManager, clazz, includeChild)
            if (childFragment != null) {
                return childFragment
            }
        }
    }
    return null
}

fun FragmentActivity.setupDefaultFragmentBackground(
    defaultBackground: Drawable? = defaultBackground()
) {
    supportFragmentManager.registerFragmentLifecycleCallbacks(
        object : FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentViewCreated(
                fm: FragmentManager,
                f: Fragment,
                v: View,
                savedInstanceState: Bundle?
            ) {
                if (v.background == null) {
                    v.background = defaultBackground
                }
            }
        }, true
    )
}

/**
 * finish Activity
 */
fun Fragment.finish() {
    requireActivity().finish()
}