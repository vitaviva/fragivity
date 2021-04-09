@file:JvmName("FragivityUtil")
@file:JvmMultifileClass

package com.github.fragivity

import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentProviderMap
import androidx.fragment.app.MyFragmentNavigator
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.createGraph
import androidx.navigation.fragment.FragmentNavigatorDestinationBuilder
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.get
import com.github.fragivity.router.createRoute
import kotlin.collections.set
import kotlin.reflect.KClass

val Fragment.navigator: FragivityNavHost
    get() = fragivityHostViewModel.navHost

val View.navigator: FragivityNavHost
    get() = fragivityHostViewModel.navHost

val NavController.navigator: FragivityNavHost
    get() = fragivityHostViewModel.navHost

/**
 * Load root fragment by factory
 */
@JvmSynthetic
inline fun <reified T : Fragment> NavHostFragment.loadRoot(noinline block: () -> T) {
    val clazz = T::class
    FragmentProviderMap[clazz.qualifiedName!!] = block
    loadRoot(clazz)
}

/**
 * Load root fragment
 */
@JvmSynthetic
fun NavHostFragment.loadRoot(root: KClass<out Fragment>) {
    loadRoot("root", root)
}

@JvmSynthetic
fun NavHostFragment.loadRoot(route: String, root: KClass<out Fragment>) {
    val activity = requireActivity()

    with(navController) {
        val fragmentNavigator = MyFragmentNavigator(activity, childFragmentManager, id)
        navigatorProvider.addNavigator(fragmentNavigator)

        val nodeViewModel = ViewModelProvider(
            this@loadRoot,
            defaultViewModelProviderFactory
        ).get(FragivityNodeViewModel::class.java)

        val startDestId = root.hashCode()
        graph = createGraph(startDestination = startDestId) {
            destination(FragmentNavigatorDestinationBuilder(
                provider[MyFragmentNavigator::class],
                startDestId, root
            ).apply {
                label = route
                deepLink(createRoute(route))
            })
            // restore destination from vm for NavController#mBackStackToRestore
            nodeViewModel.restoreDestination(this@with, this)
        }

        fragivityHostViewModel.setUpNavHost(nodeViewModel, this)
    }
}

fun FragmentActivity.findNavHostFragment(@IdRes id: Int): NavHostFragment {
    return supportFragmentManager.findFragmentById(id) as NavHostFragment
}

fun Fragment.findNavHostFragment(@IdRes id: Int): NavHostFragment {
    return childFragmentManager.findFragmentById(id) as NavHostFragment
}

/**
 * finish Activity
 */
fun Fragment.finish() {
    requireActivity().finish()
}

