package com.github.fragivity

import android.os.Bundle
import androidx.fragment.app.FragivityFragmentNavigator.Companion.KEY_ENTER_ANIM
import androidx.fragment.app.FragivityFragmentNavigator.Companion.KEY_EXIT_ANIM
import androidx.fragment.app.FragivityFragmentNavigator.Companion.KEY_POP_ENTER_ANIM
import androidx.fragment.app.FragivityFragmentNavigator.Companion.KEY_POP_EXIT_ANIM
import androidx.fragment.app.FragivityFragmentNavigator.Companion.KEY_PUSH_TO
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.clearBackStackEntry
import androidx.navigation.graphNodes
import kotlin.reflect.KClass

/**
 * 跳转到目标页面，并清空所有旧页面
 */
@JvmSynthetic
fun NavController.pushTo(clazz: KClass<out Fragment>, optionsBuilder: NavOptions.() -> Unit = {}) {
    val node = getOrCreateNode(clazz)
    node.parent?.remove(node)
    node.appendRootRoute()

    val nodeSaver = nodeSaver
    val parent = nodeSaver.parent

    checkNotNull(parent) { "NavController not bridge useful RestoreSaverNode" }

    nodeSaver.setStartNode(node.id)
    nodeSaver.onCleared()

    // 清空返回栈
    clearBackStackEntry()

    val options = navOptions(optionsBuilder)

    val bundle = Bundle()
    bundle.putAll(options.arguments)
    bundle.putBoolean(KEY_PUSH_TO, true)
    bundle.putInt(KEY_ENTER_ANIM, options.enterAnim)
    bundle.putInt(KEY_EXIT_ANIM, options.exitAnim)
    bundle.putInt(KEY_POP_ENTER_ANIM, options.popEnterAnim)
    bundle.putInt(KEY_POP_EXIT_ANIM, options.popExitAnim)

    // 重建graph
    setGraph(createGraph(node) {
        graphNodes()
            .filterNot { it.hasRootRoute() }
            .forEach { addDestination(it) }
    }, bundle)

    bridge(parent)
}