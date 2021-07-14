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
    composableInternal(route, arguments, factory)
}

private fun NavHostFragment.composableInternal(
    route: String,
    arguments: List<NamedNavArgument>,
    factory: (Bundle) -> Fragment
) = with(navController) {
    var node = graph.findNode(route)
    if (node is FragivityFragmentDestination) {
        node.factory = factory
    } else {
        node = createNavDestination(route, factory)
        graph.addDestination(node)
    }

    node.apply {
        addDeepLink(wrapDeepRoute(route))
        arguments.forEach { (argumentName, argument) ->
            addArgument(argumentName, argument)
        }
    }

    // save destination for rebuild
    navigator.saveDestination(node)
}