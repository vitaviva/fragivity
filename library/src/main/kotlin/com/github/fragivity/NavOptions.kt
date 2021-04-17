package com.github.fragivity

import android.os.Bundle
import android.view.View
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.core.os.bundleOf
import androidx.navigation.fragment.FragmentNavigator

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

    @set:AnimRes
    @set:AnimatorRes
    @set:JvmSynthetic
    var enterAnim: Int

    @set:AnimRes
    @set:AnimatorRes
    @set:JvmSynthetic
    var exitAnim: Int

    @set:AnimRes
    @set:AnimatorRes
    @set:JvmSynthetic
    var popEnterAnim: Int

    @set:AnimRes
    @set:AnimatorRes
    @set:JvmSynthetic
    var popExitAnim: Int

    @set:JvmSynthetic
    var sharedElements: List<Pair<View, String>>

}

internal class `$NavOptionsDefault` : NavOptions {
    override var launchMode: LaunchMode = LaunchMode.STANDARD
    override var popSelf: Boolean = false
    override var arguments: Bundle = Bundle.EMPTY
    override var enterAnim = -1
    override var exitAnim = -1
    override var popEnterAnim = -1
    override var popExitAnim = -1
    override var sharedElements = emptyList<Pair<View, String>>()
}

fun NavOptions.applyLaunchMode(mode: LaunchMode) {
    launchMode = mode
}

fun NavOptions.applyArguments(vararg args: Pair<String, Any?>) {
    this.arguments = bundleOf(*args)
}

fun NavOptions.applySharedElements(vararg sharedElements: Pair<View, String>) {
    this.sharedElements = sharedElementsOf(*sharedElements)
}

fun NavOptions.applySlideInOut() {
    enterAnim = R.anim.slide_in
    exitAnim = R.anim.slide_out
    popEnterAnim = R.anim.slide_in_pop
    popExitAnim = R.anim.slide_out_pop
}

fun NavOptions.applyFadeInOut() {
    enterAnim = R.anim.nav_default_enter_anim
    exitAnim = R.anim.nav_default_exit_anim
    popEnterAnim = R.anim.nav_default_enter_anim
    popExitAnim = R.anim.nav_default_exit_anim
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

internal const val KEY_POP_SELF = "Fragivity:PopSelf"

@JvmSynthetic
internal fun NavOptions.toBundle(): Bundle =
    (arguments.takeIf { it != Bundle.EMPTY } ?: Bundle()).apply {
        putBoolean(KEY_POP_SELF, this@toBundle.popSelf)
    }

@JvmSynthetic
internal fun NavOptions.totOptions(
    destinationId: Int? = null
): androidx.navigation.NavOptions =
    androidx.navigation.NavOptions.Builder().apply {
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
internal fun NavOptions.totExtras(): FragmentNavigator.Extras =
    FragmentNavigator.Extras.Builder().apply {
        sharedElements.forEach { (view, name) ->
            addSharedElement(view, name)
        }
    }.build()

fun sharedElementsOf(vararg sharedElements: Pair<View, String>) =
    mutableListOf<Pair<View, String>>().apply {
        sharedElements.forEach { (view, name) ->
            add(view to name)
        }
    }

fun navOptions(optionsBuilder: NavOptions.() -> Unit = {}): NavOptions {
    return `$NavOptionsDefault`().apply(optionsBuilder)
}
