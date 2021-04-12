@file:JvmName("FragivityUtil")
@file:JvmMultifileClass

package com.github.fragivity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentProviderMap
import androidx.fragment.app.MyFragmentNavigator
import androidx.navigation.NavDestination
import androidx.navigation.NavHost
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.plusAssign
import com.github.fragivity.util.positiveHashCode
import kotlin.collections.set
import kotlin.reflect.KClass

typealias MyNavHost = FragivityNavHost

/**
 * NavHost with viewModel to store NavDestination
 */
class FragivityNavHost(
    private val viewModel: FragivityNodeViewModel,
    navHost: NavHost
) : NavHost by navHost {

    @JvmSynthetic
    internal fun saveToViewModel(destination: NavDestination) {
        viewModel.putDestination(destination)
    }

    @JvmSynthetic
    internal fun removeFromViewModel(id: Int) {
        viewModel.removeDestination(id)
    }
}

/**
 * Navigates to a fragment by its factory
 */
@JvmSynthetic
inline fun <reified T : Fragment> FragivityNavHost.push(
    noinline optionsBuilder: NavOptions.() -> Unit = {},
    noinline block: () -> T
) {
    val clazz = T::class
    FragmentProviderMap[clazz.qualifiedName!!] = block
    push(clazz, optionsBuilder = optionsBuilder)
}

/**
 * Navigates to fragment of [clazz] by pushing it to back stack
 */
@JvmSynthetic
fun FragivityNavHost.push(
    clazz: KClass<out Fragment>,
    optionsBuilder: NavOptions.() -> Unit = {}
) {
    pushInternal(clazz, `$NavOptionsDefault`().apply(optionsBuilder))
}

@JvmSynthetic
internal fun FragivityNavHost.pushInternal(
    clazz: KClass<out Fragment>,
    navOptions: NavOptions?
) = with(navController) {
    val node = putFragment(clazz)
    if (navOptions == null) {
        navigate(node.id)
        return@with
    }

    when(navOptions.launchMode) {
        LaunchMode.STANDARD,
        LaunchMode.SINGLE_TOP -> {
            navigate(
                node.id, navOptions.toBundle(),
                navOptions.totOptions(clazz),
                navOptions.totExtras()
            )
        }
        LaunchMode.SINGLE_TASK -> {
            // curr == target || try pop to target
            if (currentDestination?.id == node.id || popBackStack(node.id, false)) {
                val navigator: Navigator<NavDestination> = navigatorProvider.getNavigator(node.navigatorName)
                if (navigator is MyFragmentNavigator) {
                    navigator.restoreTopFragment(node, navOptions.toBundle())
                }
                return@with
            }

            // create target
            navigate(
                node.id, navOptions.toBundle(),
                navOptions.totOptions(clazz),
                navOptions.totExtras()
            )
        }
    }
}

@JvmSynthetic
internal fun FragivityNavHost.putFragment(
    clazz: KClass<out Fragment>
): FragmentNavigator.Destination {
    val destId = clazz.positiveHashCode
    val graph = navController.graph
    var destination = graph.findNode(destId) as? FragmentNavigator.Destination
    if (destination == null) {
        destination = navController.createNavDestination(destId, clazz)
        graph += destination
        saveToViewModel(destination)
    }
    return destination
}

/**
 * pop current fragment from back stack
 */
@JvmSynthetic
fun FragivityNavHost.pop(): Boolean {
    return navController.popBackStack()
}

/**
 * Pop back stack to [clazz]
 */
@JvmSynthetic
fun FragivityNavHost.popTo(clazz: KClass<out Fragment>, inclusive: Boolean = false): Boolean {
    return navController.popBackStack(clazz.positiveHashCode, inclusive)
}
