@file:JvmName("FragivityUtil")
@file:JvmMultifileClass

package com.github.fragivity

import android.os.Bundle
import androidx.fragment.app.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.fragment.NavHostFragment
import kotlin.reflect.KClass

const val DEFAULT_ROOT_ROUTE = "root"

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
fun NavHostFragment.loadRoot(
    route: String,
    clazz: KClass<out Fragment>,
    block: ((Bundle) -> Fragment)? = null
) {
    loadRootInternal(route) {
        val id = clazz.positiveHashCode
        if (block != null) {
            navController.createNode(id, block)
        } else {
            navController.createNode(id, clazz)
        }
    }
}

private fun NavHostFragment.loadRootInternal(
    route: String,
    startNodeFactory: () -> NavDestination
) {
    addFragivityNavigator()
    setupGraph(startNodeFactory().apply {
        addDeepLink(createRoute(DEFAULT_ROOT_ROUTE))
        if (DEFAULT_ROOT_ROUTE != route) {
            addDeepLink(createRoute(route))
        }
    })
}

@JvmSynthetic
internal fun NavHostFragment.setupGraph(startNode: NavDestination) {
    val nodeViewModel = getViewModel<FragivityNodeViewModel>()
    with(navController) {
        val block: NavGraphBuilder.() -> Unit = {
            // restore destination from vm for NavController#mBackStackToRestore
            nodeViewModel.restoreDestination(this@with, this)
        }

        val startNodeId = nodeViewModel.startNodeId
        graph = if (startNodeId != null) {
            createGraph(startNodeId, block)
        } else {
            createGraph(startNode, block)
        }

        fragivityHostViewModel.setUpNavHost(nodeViewModel, this)
    }
}

private fun NavHostFragment.addFragivityNavigator() {
    navController.navigatorProvider.addNavigator(
        FragivityFragmentNavigator(requireContext(), childFragmentManager, id)
    )
}

private inline fun <reified T : ViewModel> NavHostFragment.getViewModel(): T {
    return ViewModelProvider(this, defaultViewModelProviderFactory).get(T::class.java)
}

@JvmSynthetic
internal fun createRoute(route: String) = "android-app://androidx.navigation.fragivity/$route"