package com.github.fragivity

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.FragivityFragmentNavigator.Companion.KEY_POP_SELF
import androidx.navigation.NavOptionsBuilder

fun moreNavOptions(builder: MoreNavOptionsFactory = {}): MoreNavOptions {
    return MoreNavOptionsBuilder(builder).build()
}

class MoreNavOptionsBuilder internal constructor(factory: MoreNavOptionsFactory) {

    private var optionsBuilders: List<NavOptionsBuilder.() -> Unit> = emptyList()
    var launchMode: LaunchMode = LaunchMode.STANDARD
    var popSelf: Boolean = false
    var arguments: Bundle = Bundle.EMPTY
    var sharedElements: Map<View, String> = emptyMap()

    init {
        MoreNavOptions.commonFactory(this)
        factory(this)
    }

    fun navOptions(optionsBuilder: NavOptionsBuilder.() -> Unit) = also {
        it.optionsBuilders += optionsBuilder
    }

    internal fun build(): MoreNavOptions {
        if (popSelf) {
            if (arguments === Bundle.EMPTY) {
                arguments = bundleOf(KEY_POP_SELF to true)
            } else {
                arguments.putBoolean(KEY_POP_SELF, true)
            }
        }

        return MoreNavOptions(
            optionsBuilders = optionsBuilders,
            launchMode = launchMode,
            popSelf = popSelf,
            arguments = arguments,
            sharedElements = sharedElements
        )
    }
}

fun MoreNavOptionsBuilder.applyLaunchMode(launchMode: LaunchMode) {
    this.launchMode = launchMode
}

fun MoreNavOptionsBuilder.applyArguments(vararg args: Pair<String, Any?>) {
    arguments = bundleOf(*args)
}

fun MoreNavOptionsBuilder.applySharedElements(vararg sharedElements: Pair<View, String>) {
    this.sharedElements = mapOf(*sharedElements)
}

fun MoreNavOptionsBuilder.applySlideInOut() = navOptions {
    anim {
        enter = R.anim.slide_in
        exit = R.anim.slide_out
        popEnter = R.anim.slide_in_pop
        popExit = R.anim.slide_out_pop
    }
}

fun MoreNavOptionsBuilder.applyVerticalInOut() = navOptions {
    anim {
        enter = R.anim.v_fragment_enter
        exit = R.anim.v_fragment_exit
        popEnter = R.anim.v_fragment_pop_enter
        popExit = R.anim.v_fragment_pop_exit
    }
}

fun MoreNavOptionsBuilder.applyHorizontalInOut() = navOptions {
    anim {
        enter = R.anim.h_fragment_enter
        exit = R.anim.h_fragment_exit
        popEnter = R.anim.h_fragment_pop_enter
        popExit = R.anim.h_fragment_pop_exit
    }
}
