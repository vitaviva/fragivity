package com.github.fragivity.router

import android.net.Uri
import android.os.Bundle
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.MyFragmentNavigator
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navigate
import androidx.navigation.popBackStack
import com.github.fragivity.*

@JvmSynthetic
fun NavHostFragment.composable(
    route: String,
    argument: NamedNavArgument,
    content: (Bundle) -> Fragment
) {
    composable(route, listOf(argument), content)
}

@JvmSynthetic
fun NavHostFragment.composable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    content: (Bundle) -> Fragment
) {
    val internalRoute = createRoute(route)
    // make id > 0
    val destinationId = internalRoute.hashCode() and Int.MAX_VALUE

    var node = navController.graph.findNode(destinationId)
    if (node is MyFragmentNavigator.MyDestination) {
        node.setContent(content)
    } else {
        node = navController.createMyNavDestination(destinationId, content)
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

@JvmSynthetic
fun MyNavHost.push(route: String, optionsBuilder: NavOptions.() -> Unit = {}) {
    push(route, navOptions(optionsBuilder))
}

@JvmSynthetic
fun MyNavHost.push(route: String, navOptions: NavOptions?) {
    navController.navigate(
        createRoute(route).toUri().toNavDeepLinkRequest(),
        navOptions ?: navOptions()
    )
}

// WARN: currentRoute should not same as route
@JvmSynthetic
fun MyNavHost.popTo(route: String, inclusive: Boolean = false) {
    navController.popBackStack(
        createRoute(route).toUri().toNavDeepLinkRequest(),
        inclusive
    )
}

private fun navOptions(optionsBuilder: NavOptions.() -> Unit = {}): NavOptions {
    return `$NavOptionsDefault`().apply(optionsBuilder)
}

private fun Uri.toNavDeepLinkRequest() = NavDeepLinkRequest.Builder.fromUri(this).build()

internal fun createRoute(route: String) = "android-app://androidx.navigation.fragivity/$route"