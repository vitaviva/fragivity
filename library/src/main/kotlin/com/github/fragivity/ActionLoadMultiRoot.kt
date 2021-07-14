package com.github.fragivity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.*
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import kotlin.reflect.KClass

/**
 * Multi Back:
 *
 * NavGraph {
 *      route = 0
 *      nodes = [
 *          NavGraph {
 *              route = 100
 *              nodes = [
 *                  NavDestination(route = xxx)
 *                  ...
 *              ]
 *          },
 *          NavGraph {
 *              route = 200
 *              nodes = [
 *                  NavDestination(route = xxx)
 *              ]
 *          },
 *      ]
 * }
 */

@JvmSynthetic
fun NavHostFragment.loadMultiRoot(vararg clazzArray: KClass<out Fragment>) {
    loadMultiRoot(0, *clazzArray)
}

@JvmSynthetic
fun NavHostFragment.loadMultiRoot(showPosition: Int, vararg clazzArray: KClass<out Fragment>) {
    loadMultiRootInternal(showPosition, clazzArray.map { clazz ->
        {
            val route = createRoute(clazz)
            navController.createNavDestination(route, clazz)
        }
    })
}

@JvmSynthetic
fun NavHostFragment.loadMultiRoot(vararg factoryArray: Pair<String, (Bundle) -> Fragment>) {
    loadMultiRoot(0, *factoryArray)
}

@JvmSynthetic
fun NavHostFragment.loadMultiRoot(
    showPosition: Int,
    vararg factoryArray: Pair<String, (Bundle) -> Fragment>
) {
    loadMultiRootInternal(showPosition, factoryArray.map { pair ->
        {
            val route = pair.first
            navController.createNavDestination(route, pair.second)
        }
    })
}

private fun NavHostFragment.loadMultiRootInternal(
    showPosition: Int = 0,
    destinations: List<() -> NavDestination>
) = setupGraph(createRoute(showPosition)) {
    destinations.forEachIndexed() { index, destinationFactory ->
        addDestination(createGraph(index, destinationFactory()))
    }
}

private fun NavHost.createGraph(position: Int, destination: NavDestination): NavGraph {
    return createGraph(route = createRoute(position), startDestination = destination.route!!) {
        addDestination(destination)
    }
}

@JvmSynthetic
fun NavController.pushTo(position: Int): Boolean {
    val destination = findDestination(createRoute(position)) ?: return false
    val builder = NavOptions.Builder().setLaunchSingleTop(true)
        .setRestoreState(true)
        .setPopUpTo(graph.findStartDestination().id, inclusive = false, saveState = true)
    return try {
        navigate(destination.id, null, builder.build())
        true
    } catch (e: IllegalArgumentException) {
        false
    }
}

@JvmSynthetic
fun NavDestination.matchDestination(position: Int): Boolean {
    val route = createRoute(position)
    return hierarchy.any { it.route == route }
}

private fun createRoute(position: Int): String {
    return (position * 100).toString()
}