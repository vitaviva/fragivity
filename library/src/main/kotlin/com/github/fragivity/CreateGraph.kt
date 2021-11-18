@file:JvmName("FragivityUtil")
@file:JvmMultifileClass

package com.github.fragivity

import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphBuilder
import androidx.navigation.createGraph

@JvmSynthetic
internal fun NavController.createGraph(
    startNode: NavDestination,
    block: NavGraphBuilder.() -> Unit = {}
): NavGraph = createGraph(startDestination = startNode.id) {
    addDestination(startNode)
    block()
}

@JvmSynthetic
internal fun NavController.createGraph(
    startNodeId: Int,
    block: NavGraphBuilder.() -> Unit = {}
): NavGraph = createGraph(startDestination = startNodeId) {
    block()
}