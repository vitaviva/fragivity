@file:JvmName("FragivityUtil")
@file:JvmMultifileClass

package com.github.fragivity

import android.os.Bundle
import androidx.fragment.app.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDestination
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import kotlin.reflect.KClass

const val DEFAULT_ROOT_ROUTE = "root"

/**
 * Load root fragment by factory
 */
inline fun <reified T : Fragment> NavHostFragment.loadRoot(noinline block: (Bundle) -> T) {
    loadRoot(T::class, block)
}

@JvmSynthetic
fun NavHostFragment.loadRoot(clazz: KClass<out Fragment>, block: (Bundle) -> Fragment) {
    loadRootInternal {
        navController.createNavDestination(clazz.positiveHashCode, block)
    }
}

/**
 * Load root fragment
 */
@JvmSynthetic
fun NavHostFragment.loadRoot(clazz: KClass<out Fragment>) {
    loadRootInternal {
        navController.createNavDestination(clazz.positiveHashCode, clazz)
    }
}

private fun NavHostFragment.loadRootInternal(
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
    startDestination.addDeepLink(createRoute(DEFAULT_ROOT_ROUTE))

    graph = createGraph(startDestination = startDestination.id) {
        addDestination(startDestination)
        // restore destination from vm for NavController#mBackStackToRestore
        nodeViewModel.restoreDestination(this@with, this)
    }

    fragivityHostViewModel.setUpNavHost(nodeViewModel, this)
}

@JvmSynthetic
internal fun createRoute(route: String) = "android-app://androidx.navigation.fragivity/$route"
