package com.github.fragivity

import android.os.Bundle
import android.view.View
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes


class NavOptionsBuilder internal constructor() {
    private val navOptions = `$NavOptionsDefault`()

    fun setLaunchMode(mode: LaunchMode) =
        also { navOptions.launchMode = mode }

    fun setArguments(bundle: Bundle) =
        also { navOptions.arguments = bundle }

    fun setEnterAnim(@AnimRes @AnimatorRes enterAnim: Int) =
        also { navOptions.enterAnim = enterAnim }

    fun setExitAnim(@AnimRes @AnimatorRes exitAnim: Int) =
        also { navOptions.exitAnim = exitAnim }

    fun setPopEnterAnim(@AnimRes @AnimatorRes popEnterAnim: Int) =
        also { navOptions.popEnterAnim = popEnterAnim }

    fun setPopExitAnim(@AnimRes @AnimatorRes popExitAnim: Int) =
        also { navOptions.popExitAnim = popExitAnim }

    fun setSharedElements(sharedElements: Map<View, String>) =
        also {
            val list = mutableListOf<Pair<View, String>>()
            for ((view, name) in sharedElements) {
                list.add(Pair(view, name))
            }
            navOptions.sharedElements = list
        }

    fun build(): NavOptions {
        return navOptions
    }
}