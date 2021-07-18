package com.github.fragivity

import androidx.navigation.*

@JvmSynthetic
internal fun NavController.createGraph(
    startNode: NavDestination,
    block: NavGraphBuilder.() -> Unit = {}
): NavGraph = createGraph(startDestination = startNode.id) {
    addDestination(startNode)
    block()
}