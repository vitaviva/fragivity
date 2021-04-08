@file:JvmName("FragivityUtil")
@file:JvmMultifileClass

package com.github.fragivity

import android.view.View
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentProviderMap
import androidx.fragment.app.MyFragmentNavigator
import androidx.navigation.createGraph
import androidx.navigation.fragment.FragmentNavigatorDestinationBuilder
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.get
import com.github.fragivity.router.createRoute
import kotlin.collections.set
import kotlin.reflect.KClass

val Fragment.navigator: MyNavHost
    get() = requireActivity().getFragivityViewModel().getNavHost(this)

val View.navigator: MyNavHost
    get() = (context as ComponentActivity).getFragivityViewModel().getNavHost(this)

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

        val viewModel = activity.getFragivityViewModel()
        viewModel.setNavController(this)

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
            viewModel.restoreDestination(this)
        }
    }
}

/**
 * Load root fragment by factory
 */
@JvmSynthetic
inline fun <reified T : Fragment> NavHostFragment.loadRoot(
    noinline block: () -> T
) {
    val clazz = T::class
    FragmentProviderMap[clazz.qualifiedName!!] = block
    loadRoot(T::class)
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

