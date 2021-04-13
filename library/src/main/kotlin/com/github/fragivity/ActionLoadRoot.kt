@file:JvmName("FragivityUtil")
@file:JvmMultifileClass

package com.github.fragivity

import android.os.Bundle
import androidx.fragment.app.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDestination
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import com.github.fragivity.util.positiveHashCode
import kotlin.reflect.KClass

/**
 * Load root fragment by factory
 */
inline fun <reified T : Fragment> NavHostFragment.loadRoot(
    route: String = "root",
    noinline block: (Bundle) -> T
) {
    loadRoot(route, T::class, block)
}

fun NavHostFragment.loadRoot(
    clazz: KClass<out Fragment>,
    block: (Bundle) -> Fragment
) {
    loadRoot("root", clazz, block)
}

fun NavHostFragment.loadRoot(
    route: String,
    clazz: KClass<out Fragment>,
    block: (Bundle) -> Fragment
) {
    loadRootInternal(route) {
        navController.createNavDestination(clazz.positiveHashCode, block)
    }
}

/**
 * Load root fragment
 */
fun NavHostFragment.loadRoot(root: KClass<out Fragment>) {
    loadRoot("root", root)
}

fun NavHostFragment.loadRoot(route: String, clazz: KClass<out Fragment>) {
    loadRootInternal(route) {
        navController.createNavDestination(clazz.positiveHashCode, clazz)
    }
}

private fun NavHostFragment.loadRootInternal(
    route: String,
    startDestinationFactory: () -> NavDestination
) = with(navController) {
    navigatorProvider.addNavigator(
        FragivityFragmentNavigator(requireContext(), childFragmentManager, id)
    )

    val nodeViewModel = ViewModelProvider(
        this@loadRootInternal,
        defaultViewModelProviderFactory
    ).get(FragivityNodeViewModel::class.java)

    val startDestination = startDestinationFactory.invoke()
    startDestination.addDeepLink(createRoute(route))

    graph = createGraph(startDestination = startDestination.id) {
        addDestination(startDestination)
        // restore destination from vm for NavController#mBackStackToRestore
        nodeViewModel.restoreDestination(this@with, this)
    }

    fragivityHostViewModel.setUpNavHost(nodeViewModel, this)
}

internal fun createRoute(route: String) = "android-app://androidx.navigation.fragivity/$route"
