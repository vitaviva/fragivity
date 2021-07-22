@file:JvmName("FragivityUtil")
@file:JvmMultifileClass

package com.github.fragivity

import android.os.Bundle
import androidx.fragment.app.FragivityFragmentNavigator.Companion.KEY_ENTER_ANIM
import androidx.fragment.app.FragivityFragmentNavigator.Companion.KEY_EXIT_ANIM
import androidx.fragment.app.FragivityFragmentNavigator.Companion.KEY_POP_ENTER_ANIM
import androidx.fragment.app.FragivityFragmentNavigator.Companion.KEY_POP_EXIT_ANIM
import androidx.fragment.app.FragivityFragmentNavigator.Companion.KEY_PUSH_TO
import androidx.fragment.app.Fragment
import androidx.navigation.*
import kotlin.reflect.KClass

@JvmSynthetic
fun NavController.pushTo(route: String, optionsBuilder: NavOptions.() -> Unit = {}) {
    pushTo(route, navOptions(optionsBuilder))
}

@JvmSynthetic
fun NavController.pushTo(route: String, navOptions: NavOptions?) {
    val (node, matchingArgs) = findDestinationAndArgs(route.toRequest()) ?: return
    pushToInternal(node, navOptions, matchingArgs)
}

@JvmSynthetic
fun NavController.pushTo(
    clazz: KClass<out Fragment>,
    optionsBuilder: NavOptions.() -> Unit = {}
) {
    pushTo(clazz, navOptions(optionsBuilder))
}

@JvmSynthetic
fun NavController.pushTo(clazz: KClass<out Fragment>, navOptions: NavOptions?) {
    pushToInternal(getOrCreateNode(clazz), navOptions)
}

@JvmSynthetic
inline fun <reified T : Fragment> NavController.pushTo(
    noinline optionsBuilder: NavOptions.() -> Unit = {},
    noinline block: (Bundle) -> T
) {
    pushTo(T::class, navOptions(optionsBuilder), block)
}

@JvmSynthetic
fun NavController.pushTo(
    clazz: KClass<out Fragment>,
    navOptions: NavOptions?,
    factory: (Bundle) -> Fragment
) {
    pushToInternal(getOrCreateNode(clazz, factory), navOptions)
}

/**
 * 跳转到目标页面，并清空所有旧页面
 */
private fun NavController.pushToInternal(
    node: NavDestination,
    navOptions: NavOptions?,
    matchingArgs: Bundle? = null
) {
    node.parent?.remove(node)
    node.appendRootRoute()

    val nodeSaver = nodeSaver
    val parent = nodeSaver.parent

    checkNotNull(parent) { "NavController not bridge useful RestoreSaverNode" }

    nodeSaver.setStartNode(node.id)
    nodeSaver.onCleared()

    // 清空返回栈
    clearBackStackEntry()

    val bundle = Bundle()
    bundle.putBoolean(KEY_PUSH_TO, true)
    if (navOptions != null) {
        bundle.putAll(navOptions.arguments)
        bundle.putInt(KEY_ENTER_ANIM, navOptions.enterAnim)
        bundle.putInt(KEY_EXIT_ANIM, navOptions.exitAnim)
        bundle.putInt(KEY_POP_ENTER_ANIM, navOptions.popEnterAnim)
        bundle.putInt(KEY_POP_EXIT_ANIM, navOptions.popExitAnim)
    }
    if (matchingArgs != null) {
        bundle.putAll(matchingArgs)
    }

    // 重建graph
    setGraph(createGraph(node) {
        graphNodes()
            .filterNot { it.hasRootRoute() }
            .forEach { addDestination(it) }
    }, bundle)

    bridge(parent)
}