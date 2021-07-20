package com.github.fragivity

import androidx.core.os.bundleOf
import androidx.fragment.app.FragivityFragmentNavigator.Companion.KEY_PUSH_TO
import androidx.fragment.app.Fragment
import androidx.navigation.clearBackStackEntry
import kotlin.reflect.KClass

/**
 * 跳转到目标页面，并清空所有旧页面
 */
@JvmSynthetic
fun FragivityNavHost.pushTo(clazz: KClass<out Fragment>) {

    val node = getOrCreateNode(clazz)

    // 删除原有parent(NavGraph)
    node.parent?.remove(node)

    // 清空所有node
    viewModel.clearNodes()

    // 重新保存
    addNode(node)

    // 主动清空旧NavHost防止内存泄漏
    val viewModel = viewModel
    val navController = navController
    onCleared()

    // 修改开始id用于重建
    viewModel.startNodeId = node.id

    with(navController) {
        // 清空返回栈
        clearBackStackEntry()

        // 重建graph
        setGraph(createGraph(node), bundleOf(
            KEY_PUSH_TO to true
        ))

        // 重新绑定nav范围的viewModel
        fragivityHostViewModel.setUpNavHost(viewModel, this)
    }
}