package com.github.fragivity

import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.fragment.app.Fragment
import kotlin.reflect.KClass

class NavOptions {

    var launchMode: LaunchMode = LaunchMode.STANDARD

    @AnimRes
    @AnimatorRes
    var enterAnim = -1

    @AnimRes
    @AnimatorRes
    var exitAnim = -1

    @AnimRes
    @AnimatorRes
    var popEnterAnim = -1

    @AnimRes
    @AnimatorRes
    var popExitAnim = -1
}

@PublishedApi
internal fun convertNavOptions(
    clazz: KClass<out Fragment>,
    navOptions: NavOptions
): androidx.navigation.NavOptions {
    return androidx.navigation.NavOptions.Builder().apply {
        setEnterAnim(navOptions.enterAnim)
        setExitAnim(navOptions.exitAnim)
        setPopEnterAnim(navOptions.popEnterAnim)
        setPopExitAnim(navOptions.popExitAnim)
        setLaunchSingleTop(navOptions.launchMode == LaunchMode.SINGLE_TOP)
        if (navOptions.launchMode == LaunchMode.SINGLE_TASK) {
            setPopUpTo(clazz.hashCode(), true)
        }
    }.build()
}

enum class LaunchMode {
    STANDARD, SINGLE_TOP, SINGLE_TASK
}