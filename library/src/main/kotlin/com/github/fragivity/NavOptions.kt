package com.github.fragivity

import android.os.Bundle
import android.view.View
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.core.os.bundleOf
import androidx.fragment.app.FragivityFragmentNavigator.Companion.KEY_POP_SELF
import androidx.navigation.fragment.FragmentNavigator

private typealias NavigationOptionsBuilder = androidx.navigation.NavOptions.Builder

enum class LaunchMode {
    STANDARD, SINGLE_TOP, SINGLE_TASK
}

interface NavOptions {

    @set:JvmSynthetic
    var launchMode: LaunchMode

    @set:JvmSynthetic
    var popSelf: Boolean

    @set:JvmSynthetic
    var arguments: Bundle

    @set:[JvmSynthetic AnimRes AnimatorRes]
    var enterAnim: Int

    @set:[JvmSynthetic AnimRes AnimatorRes]
    var exitAnim: Int

    @set:[JvmSynthetic AnimRes AnimatorRes]
    var popEnterAnim: Int

    @set:[JvmSynthetic AnimRes AnimatorRes]
    var popExitAnim: Int

    @set:JvmSynthetic
    var sharedElements: Map<View, String>


    fun interface Factory {
        fun create(): NavOptions
    }

    companion object : Factory {

        private var navOptionsFactory: Factory? = null

        fun setNavOptionsFactory(factory: Factory?) {
            navOptionsFactory = factory
        }

        override fun create(): NavOptions {
            return navOptionsFactory?.create() ?: navOptions(isRaw = true)
        }
    }
}

@JvmSynthetic
internal fun NavOptions.toBundle() =
    if (popSelf) {
        Bundle(arguments).apply { putBoolean(KEY_POP_SELF, true) }
    } else arguments

@JvmSynthetic
internal fun NavOptions.totOptions(destinationId: Int? = null) =
    NavigationOptionsBuilder().apply {
        setEnterAnim(enterAnim)
        setExitAnim(exitAnim)
        setPopEnterAnim(popEnterAnim)
        setPopExitAnim(popExitAnim)
        setLaunchSingleTop(launchMode == LaunchMode.SINGLE_TOP)
        if (launchMode == LaunchMode.SINGLE_TASK && destinationId != null) {
            setPopUpTo(destinationId, true)
        }
    }.build()

@JvmSynthetic
internal fun NavOptions.totExtras() =
    FragmentNavigator.Extras.Builder()
        .addSharedElements(sharedElements)
        .build()

fun NavOptions.applyLaunchMode(mode: LaunchMode) {
    this.launchMode = mode
}

fun NavOptions.applyArguments(vararg args: Pair<String, Any?>) {
    this.arguments = bundleOf(*args)
}

fun NavOptions.applySharedElements(vararg sharedElements: Pair<View, String>) {
    this.sharedElements = mapOf(*sharedElements)
}

fun NavOptions.applySlideInOut() {
    enterAnim = R.anim.slide_in
    exitAnim = R.anim.slide_out
    popEnterAnim = R.anim.slide_in_pop
    popExitAnim = R.anim.slide_out_pop
}

fun NavOptions.applyVerticalInOut() {
    enterAnim = R.anim.v_fragment_enter
    exitAnim = R.anim.v_fragment_exit
    popEnterAnim = R.anim.v_fragment_pop_enter
    popExitAnim = R.anim.v_fragment_pop_exit
}

fun NavOptions.applyHorizontalInOut() {
    enterAnim = R.anim.h_fragment_enter
    exitAnim = R.anim.h_fragment_exit
    popEnterAnim = R.anim.h_fragment_pop_enter
    popExitAnim = R.anim.h_fragment_pop_exit
}

fun navOptions(optionsBuilder: NavOptions.() -> Unit = {}): NavOptions {
    return NavOptions.create().apply(optionsBuilder)
}

fun navOptions(isRaw: Boolean, optionsBuilder: NavOptions.() -> Unit = {}): NavOptions {
    return (if (isRaw) NavOptionsDefault() else NavOptions.create()).apply(optionsBuilder)
}

private class NavOptionsDefault : NavOptions {
    override var launchMode: LaunchMode = LaunchMode.STANDARD
    override var popSelf: Boolean = false
    override var arguments: Bundle = Bundle.EMPTY
    override var enterAnim = -1
    override var exitAnim = -1
    override var popEnterAnim = -1
    override var popExitAnim = -1
    override var sharedElements: Map<View, String> = emptyMap()
}
