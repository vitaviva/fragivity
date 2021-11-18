package com.github.fragivity

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.navigation.AnimBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.fragment.FragmentNavigator

enum class LaunchMode {
    STANDARD, SINGLE_TOP, SINGLE_TASK
}

class MoreNavOptions internal constructor(
    internal val optionsBuilders: List<NavOptionsBuilder.() -> Unit>,
    val launchMode: LaunchMode,
    val popSelf: Boolean,
    val arguments: Bundle,
    val sharedElements: Map<View, String>,
    val anim: AnimBuilder,
) {
    companion object {
        internal var commonFactory: MoreNavOptionsFactory = {}
    }
}

@JvmSynthetic
internal fun MoreNavOptions.toBundle() = arguments

@JvmSynthetic
internal fun MoreNavOptions.totOptions(@IdRes destinationId: Int? = null) = androidx.navigation.navOptions {
    optionsBuilders.forEach { builder -> builder() }

    launchSingleTop = launchMode == LaunchMode.SINGLE_TOP
    if (launchMode == LaunchMode.SINGLE_TASK && destinationId != null) {
        popUpTo(destinationId) {
            inclusive = true
        }
    }

    anim {
        enter = anim.enter
        exit = anim.exit
        popEnter = anim.popEnter
        popExit = anim.popExit
    }
}

@JvmSynthetic
internal fun MoreNavOptions.totExtras() =
    FragmentNavigator.Extras.Builder()
        .addSharedElements(sharedElements)
        .build()
