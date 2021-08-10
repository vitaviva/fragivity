@file:JvmName("FragivityUtil")
@file:JvmMultifileClass

package com.github.fragivity

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import kotlin.reflect.KClass

fun FragmentActivity.fragmentContainerView(@IdRes id: Int): FragmentContainerView {
    return FragmentContainerView(this).apply { this.id = id }
}

fun FragmentActivity.findNavHostFragment(@IdRes id: Int): NavHostFragment? {
    return supportFragmentManager.findFragmentById(id) as? NavHostFragment
}

fun FragmentActivity.findOrCreateNavHostFragment(
    @IdRes id: Int,
    isReport: Boolean = true
): NavHostFragment {
    var navHostFragment = findNavHostFragment(id)
    if (navHostFragment == null) {
        navHostFragment = supportFragmentManager.createNavHostFragment(id, isReport)
    }
    return navHostFragment
}

fun <T : Fragment> FragmentActivity.findFragment(
    clazz: KClass<T>,
    includeChild: Boolean = true
): T? {
    return findFragment(supportFragmentManager, clazz, includeChild)
}

@JvmOverloads
fun FragmentActivity.setupDefaultFragmentBackground(
    defaultBackground: Drawable? = defaultBackground()
) {
    if (defaultBackground == null) return
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