@file:JvmName("FragivityUtil")
@file:JvmMultifileClass

package com.github.fragivity

import android.os.Bundle
import androidx.core.net.toUri
import androidx.fragment.app.FragivityFragmentNavigator
import androidx.fragment.app.Fragment
import androidx.navigation.*
import androidx.navigation.fragment.FragmentNavigator
import com.github.fragivity.util.positiveHashCode
import kotlin.reflect.KClass

@JvmSynthetic
fun FragivityNavHost.push(route: String, optionsBuilder: NavOptions.() -> Unit = {}) {
    push(route, navOptions(optionsBuilder))
}

@JvmSynthetic
fun FragivityNavHost.push(route: String, navOptions: NavOptions?) {
    navController.navigate(
        NavDeepLinkRequest.Builder.fromUri(createRoute(route).toUri()).build(),
        navOptions
    )
}

/**
 * Navigates to fragment of [clazz] by pushing it to back stack
 */
@JvmSynthetic
fun FragivityNavHost.push(
    clazz: KClass<out Fragment>,
    optionsBuilder: NavOptions.() -> Unit = {}
) {
    push(clazz, navOptions(optionsBuilder))
}

fun FragivityNavHost.push(
    clazz: KClass<out Fragment>,
    navOptions: NavOptions?
) {
    pushInternal(putFragment(clazz), navOptions)
}

/**
 * Navigates to a fragment by its factory
 */
@JvmSynthetic
inline fun <reified T : Fragment> FragivityNavHost.push(
    noinline optionsBuilder: NavOptions.() -> Unit = {},
    noinline block: (Bundle) -> T
) {
    push(T::class, navOptions(optionsBuilder), block)
}

fun FragivityNavHost.push(
    clazz: KClass<out Fragment>,
    navOptions: NavOptions?,
    factory: (Bundle) -> Fragment
) {
    pushInternal(putFragment(clazz, factory), navOptions)
}

@JvmSynthetic
internal fun FragivityNavHost.putFragment(
    clazz: KClass<out Fragment>,
    factory: ((Bundle) -> Fragment)? = null
): FragmentNavigator.Destination {
    val destId = clazz.positiveHashCode
    val graph = navController.graph
    var destination = graph.findNode(destId) as? FragmentNavigator.Destination
    if (destination == null) {
        destination = if (factory != null) {
            navController.createNavDestination(destId, factory)
        } else {
            navController.createNavDestination(destId, clazz)
        }
        graph += destination
        saveToViewModel(destination)
    }
    return destination
}

@JvmSynthetic
private fun FragivityNavHost.pushInternal(
    node: FragmentNavigator.Destination,
    navOptions: NavOptions?
) = with(navController) {
    if (navOptions == null) {
        navigate(node.id)
        return@with
    }

    when (navOptions.launchMode) {
        LaunchMode.STANDARD,
        LaunchMode.SINGLE_TOP -> {
            navigate(
                node.id, navOptions.toBundle(),
                navOptions.totOptions(node.id),
                navOptions.totExtras()
            )
        }
        LaunchMode.SINGLE_TASK -> {
            // curr == target || try pop to target
            if (currentDestination?.id == node.id || popBackStack(node.id, false)) {
                val navigator: Navigator<NavDestination> =
                    navigatorProvider.getNavigator(node.navigatorName)
                if (navigator is FragivityFragmentNavigator) {
                    navigator.restoreTopFragment(node, navOptions.toBundle())
                }
                return@with
            }

            // create target
            navigate(
                node.id, navOptions.toBundle(),
                navOptions.totOptions(node.id),
                navOptions.totExtras()
            )
        }
    }
}