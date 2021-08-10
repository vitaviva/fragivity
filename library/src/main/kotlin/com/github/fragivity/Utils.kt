@file:JvmName("FragivityUtil")
@file:JvmMultifileClass

package com.github.fragivity

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.use
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commitNow
import androidx.fragment.app.setupReportFragmentManager
import androidx.navigation.fragment.NavHostFragment
import kotlin.reflect.KClass

/**
 * string hash容易冲突，使用System.identityHashCode离散hash
 * 2021.08.06:
 *  修改系统字体大小以后，System.identityHashCode结果会不同，暂时换回hashCode()
 * 2021.08.06v2:
 *  修改系统显示大小以后，KClass<out Fragment>.hashCode()也会不同，暂时使用this.java.name.hashCode()
 */
internal inline val KClass<out Fragment>.positiveHashCode: Int
    get() = this.java.name.hashCode()

internal inline val Any.positiveHashCode: Int
    get() = this.hashCode()

@JvmSynthetic
internal fun View.appendBackground() {
    background = context.defaultBackground()
}

@JvmName("getDefaultBackground")
internal fun Context.defaultBackground(): Drawable? {
    return theme.obtainStyledAttributes(intArrayOf(android.R.attr.windowBackground)).use {
        val background = it.getResourceId(0, 0)
        ContextCompat.getDrawable(this, background)
    }
}

@JvmSynthetic
internal operator fun Bundle?.plus(optionArgs: Bundle?): Bundle? {
    if (optionArgs == null) return this
    if (this == null) return optionArgs
    return Bundle().apply {
        putAll(optionArgs)
        putAll(this@plus)
    }
}

@JvmSynthetic
internal operator fun Fragment.plusAssign(newBundle: Bundle?) {
    if (newBundle == null) {
        return
    }

    val oldArgs: Bundle? = this.arguments
    if (oldArgs == null) {
        this.arguments = newBundle
        return
    }

    oldArgs.putAll(newBundle)
}

@JvmSynthetic
internal fun ArrayDeque<Int>.replaceAll(array: IntArray?) {
    if (array == null) return
    clear()
    for (value in array) {
        add(value)
    }
}

internal fun <T : Fragment> findFragment(
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

@JvmSynthetic
internal fun FragmentManager.createNavHostFragment(
    @IdRes id: Int, isReport: Boolean
): NavHostFragment {
    val navHostFragment = NavHostFragment.create(0)
    if (isReport) {
        navHostFragment.setupReportFragmentManager()
    }
    commitNow(true) {
        setReorderingAllowed(true)
        add(id, navHostFragment)
        setPrimaryNavigationFragment(navHostFragment)
    }
    return navHostFragment
}