@file:JvmName("FragivityUtil")
@file:JvmMultifileClass

package com.github.fragivity

import android.os.Bundle
import androidx.fragment.app.FragivityFragmentNavigator
import androidx.fragment.app.Fragment
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.fragment.NavHostFragment
import kotlin.reflect.KClass

/**
 * Load root fragment by factory
 */
inline fun <reified T : Fragment> NavHostFragment.loadRoot(
    route: String = DEFAULT_ROOT_ROUTE,
    noinline block: (Bundle) -> T
) {
    loadRoot(route, T::class, block)
}

/**
 * Load root fragment
 */
@JvmSynthetic
fun NavHostFragment.loadRoot(clazz: KClass<out Fragment>) {
    loadRoot(DEFAULT_ROOT_ROUTE, clazz)
}

@JvmSynthetic
internal fun NavHostFragment.loadRoot(
    clazz: KClass<out Fragment>,
    block: ((Bundle) -> Fragment)? = null
) {
    loadRoot(DEFAULT_ROOT_ROUTE, clazz, block)
}

@JvmSynthetic
fun NavHostFragment.loadRoot(
    route: String,
    clazz: KClass<out Fragment>,
    block: ((Bundle) -> Fragment)? = null
) {
    loadRootInternal(route) {
        val id = clazz.positiveHashCode
        navController.createNode(id, clazz, block, label = route)
    }
}

private fun NavHostFragment.loadRootInternal(
    route: String,
    startNodeFactory: () -> NavDestination
) {
    addFragivityNavigator()
    setupGraph(startNodeFactory().apply {
        appendDeepRoute(route)
        appendRootRoute()
    })
}

@JvmSynthetic
internal fun NavHostFragment.setupGraph(startNode: NavDestination) {
    val nodeSaver = nodeSaver

    val block: NavGraphBuilder.() -> Unit = {
        nodeSaver.restoreNodes(navController, this)
    }

    with(navController) {
        val startNodeId = nodeSaver.startNodeId
        graph = if (startNodeId != null) {
            createGraph(startNodeId, block)
        } else {
            createGraph(startNode, block)
        }
    }

    navController.bridge(nodeSaver)
}

private fun NavHostFragment.addFragivityNavigator() {
    navController.navigatorProvider.addNavigator(
        FragivityFragmentNavigator(requireContext(), childFragmentManager, id)
    )
}