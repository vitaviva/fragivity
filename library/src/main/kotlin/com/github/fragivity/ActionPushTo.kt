package com.github.fragivity

import androidx.core.os.bundleOf
import androidx.fragment.app.FragivityFragmentNavigator.Companion.KEY_PUSH_TO
import androidx.fragment.app.Fragment
import androidx.navigation.clearBackStackEntry
import androidx.navigation.graphNodes
import kotlin.reflect.KClass

/**
 * 跳转到目标页面，并清空所有旧页面
 */
@JvmSynthetic
fun FragivityNavHost.pushTo(clazz: KClass<out Fragment>) {
    val node = getOrCreateNode(clazz)
    node.parent?.remove(node)
    node.appendRootRoute()

    val nodeSaver = nodeSaver
    val navController = navController

    // 主动清空旧NavHost防止内存泄漏
    onCleared()

    // 修改开始id用于重建
    setStartNode(node.id)

    with(navController) {
        // 清空返回栈
        clearBackStackEntry()

        val oldNodes = graphNodes()

        // 重建graph
        setGraph(createGraph(node) {
            oldNodes.filterNot { it.hasRootRoute() }
                .forEach { addDestination(it) }
        }, bundleOf(KEY_PUSH_TO to true))

        setupNodeSaver(nodeSaver)
    }
}