package com.github.fragivity

import android.os.Bundle
import android.view.View
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes

class NavOptionsBuilder internal constructor() {
    private val navOptions = navOptions()

    fun setLaunchMode(mode: LaunchMode) =
        also { navOptions.launchMode = mode }

    fun popSelf(enable: Boolean) =
        also { navOptions.popSelf = enable }

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
        also { navOptions.sharedElements = sharedElements }

    fun build(): NavOptions {
        return navOptions
    }
}