package com.github.fragivity

import android.os.Bundle
import android.view.View
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes

class NavOptionsBuilder internal constructor() {

    private val navOptions: MoreNavOptionsBuilder = Fragivity.factoryBuilder.navOptions.copy()

    fun setLaunchMode(mode: LaunchMode) =
        also { navOptions.launchMode = mode }

    fun popSelf(enable: Boolean) =
        also { navOptions.popSelf = enable }

    fun setArguments(bundle: Bundle) =
        also { navOptions.arguments = bundle }

    fun setEnterAnim(@AnimRes @AnimatorRes enterAnim: Int) =
        also { navOptions.anim.enter = enterAnim }

    fun setExitAnim(@AnimRes @AnimatorRes exitAnim: Int) =
        also { navOptions.anim.exit = exitAnim }

    fun setPopEnterAnim(@AnimRes @AnimatorRes popEnterAnim: Int) =
        also { navOptions.anim.popEnter = popEnterAnim }

    fun setPopExitAnim(@AnimRes @AnimatorRes popExitAnim: Int) =
        also { navOptions.anim.popExit = popExitAnim }

    fun setSharedElements(sharedElements: Map<View, String>) =
        also { navOptions.sharedElements = sharedElements }

    fun build(): MoreNavOptions {
        return navOptions.build()
    }
}