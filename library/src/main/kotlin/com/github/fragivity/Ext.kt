@file:JvmName("FragivityUtil")
@file:JvmMultifileClass

package com.github.fragivity

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentProviderMap
import androidx.fragment.app.MyFragmentNavigator
import androidx.fragment.app.ReportFragment
import androidx.navigation.*
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorDestinationBuilder
import androidx.navigation.fragment.NavHostFragment
import com.github.fragivity.deeplink.getRouteUri
import com.github.fragivity.router.createRoute
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
    val context = activity ?: return

    navController.apply {
        navigatorProvider.addNavigator(
            MyFragmentNavigator(
                context,
                childFragmentManager,
                id
            )
        )
        val startDestId = root.hashCode()
        graph = createGraph(startDestination = startDestId) {
            destination(
                FragmentNavigatorDestinationBuilder(
                    provider[MyFragmentNavigator::class],
                    startDestId,
                    root
                ).apply {
                    label = "home"
                    deepLink(createRoute("root"))
                })

        }.also { graph ->
            //restore destination from vm for NavController#mBackStackToRestore
            val activity = requireActivity()
            val viewModel = activity.getFragivityViewModel()
            viewModel.setNavController(this)
            viewModel.nodesIterator().forEach {
                it.removeFromParent()
                graph += it
            }
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

@JvmSynthetic
internal fun NavController.createNavDestination(
    destId: Int,
    clazz: KClass<out Fragment>
): FragmentNavigator.Destination {
    return (FragmentNavigatorDestinationBuilder(
        navigatorProvider[MyFragmentNavigator::class],
        destId,
        clazz
    ).apply {
        label = clazz.qualifiedName
        getRouteUri(clazz)?.let {
            deepLink {
                uriPattern = it
            }
        }
    }).build()
}

@JvmSynthetic
internal fun NavController.createMyNavDestination(
    destinationId: Int,
    content: ((Bundle) -> Fragment)? = null
) = MyFragmentNavigator.MyDestination(
    navigatorProvider[MyFragmentNavigator::class],
    content
).apply { id = destinationId }

internal fun Fragment.requireParentFragmentManager() =
    if (parentFragment is ReportFragment) requireParentFragment().parentFragmentManager else parentFragmentManager


internal fun Fragment.requirePreviousFragment(): Fragment? {
    if (this is ReportFragment) return parentFragmentManager.fragments.firstOrNull()
    val fragmentList: List<Fragment> = requireParentFragmentManager().fragments
    val index = fragmentList.indexOf(parentFragment)
    return if (index > 0) fragmentList[index - 1].childFragmentManager.fragments[0] else null
}

/**
 * finish Activity
 */
fun Fragment.finish() {
    requireActivity().finish()
}

