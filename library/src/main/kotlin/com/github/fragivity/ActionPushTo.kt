package com.github.fragivity

import androidx.core.os.bundleOf
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
fun NavController.pushTo(clazz: KClass<out Fragment>) {
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

    val oldNodes = graphNodes()

    // 重建graph
    setGraph(createGraph(node) {
        oldNodes.filterNot { it.hasRootRoute() }
            .forEach { addDestination(it) }
    }, bundleOf(KEY_PUSH_TO to true))

    bridge(parent)
}