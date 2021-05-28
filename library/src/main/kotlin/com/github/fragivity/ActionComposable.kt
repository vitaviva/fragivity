@file:JvmName("FragivityUtil")
@file:JvmMultifileClass

package com.github.fragivity

import android.os.Bundle
import androidx.fragment.app.FragivityFragmentDestination
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment

@JvmSynthetic
fun NavHostFragment.composable(
    route: String,
    argument: NamedNavArgument,
    factory: (Bundle) -> Fragment
) {
    composable(route, listOf(argument), factory)
}

@JvmSynthetic
fun NavHostFragment.composable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    factory: (Bundle) -> Fragment
) {
    composableInternal(route, route.positiveHashCode, arguments, factory)
}

private fun NavHostFragment.composableInternal(
    route: String,
    destinationId: Int,
    arguments: List<NamedNavArgument>,
    factory: (Bundle) -> Fragment
) {
    val internalRoute = createRoute(route)

    var node = navController.graph.findNode(destinationId)
    if (node is FragivityFragmentDestination) {
        node.factory = factory
    } else {
        node = navController.createNavDestination(destinationId, factory)
        navController.graph.addDestination(node)
    }

    node.apply {
        addDeepLink(internalRoute)
        arguments.forEach { (argumentName, argument) ->
            addArgument(argumentName, argument)
        }
    }

    // save destination for rebuild
    navController.fragivityHostViewModel.navHost.saveToViewModel(node)
}