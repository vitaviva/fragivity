@file:JvmName("FragivityUtil")
@file:JvmMultifileClass
package com.github.fragivity

import android.content.Context
import androidx.collection.keyIterator
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentProviderMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDestination
import androidx.navigation.NavHost
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.plusAssign
import kotlin.collections.set
import kotlin.reflect.KClass


/**
 * NavHost with viewModel to store NavDestination
 */
class MyNavHost(
    context: Context, navHost: NavHost
) : NavHost by navHost {

    private val _activity = context as FragmentActivity

    private val _vm: MyViewModel by ViewModelLazy(
        MyViewModel::class,
        { _activity.viewModelStore },
        { assertionFactory }
    )

    @JvmSynthetic
    internal fun saveToViewModel(destination: NavDestination) {
        with(_vm) {
            if (nodes.keyIterator().asSequence().any {
                    it == destination.id
                }) return
            nodes.put(destination.id, destination)
            saveState()
        }
    }

    @JvmSynthetic
    internal fun removeFromViewModel(id: Int) {
        _vm.nodes.remove(id)
    }

    companion object {
        private val assertionFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                throw IllegalStateException("${modelClass.simpleName} should be created in loadRoot.")
            }
        }
    }
}


/**
 * Navigates to fragment of [clazz] by pushing it to back stack
 */
@JvmSynthetic
fun MyNavHost.push(
    clazz: KClass<out Fragment>,
    optionsBuilder: NavOptions.() -> Unit = {}
) =
    pushInternal(clazz, `$NavOptionsDefault`().apply(optionsBuilder))

/**
 * Navigates to a fragment by its factory
 */
@JvmSynthetic
inline fun <reified T : Fragment> MyNavHost.push(
    noinline optionsBuilder: NavOptions.() -> Unit = {},
    noinline block: () -> T
) {
    val clazz = T::class
    FragmentProviderMap[clazz.qualifiedName!!] = block
    push(clazz, optionsBuilder = optionsBuilder)
}

@JvmSynthetic
internal fun MyNavHost.pushInternal(
    clazz: KClass<out Fragment>,
    navOptions: NavOptions?
) = with(navController) {
    val node = putFragment(clazz)
    if (navOptions == null)
        navigate(node.id)
    else
        navigate(
            node.id, navOptions.toBundle(),
            navOptions.totOptions(clazz),
            navOptions.totExtras()
        )
}


/**
 * pop current fragment from back stack
 */
@JvmSynthetic
fun MyNavHost.pop() {
    navController.popBackStack()
}


/**
 * Pop back stack to [clazz]
 */
@JvmSynthetic
fun MyNavHost.popTo(clazz: KClass<out Fragment>) {
    navController.popBackStack(clazz.hashCode(), false)
}

@JvmSynthetic
internal fun MyNavHost.putFragment(
    clazz: KClass<out Fragment>
): FragmentNavigator.Destination {
    val destId = clazz.hashCode()
    val graph = navController.graph
    var destination = graph.findNode(destId) as? FragmentNavigator.Destination
    if (destination == null) {
        destination = navController.createNavDestination(destId, clazz)
        graph.plusAssign(destination)
        saveToViewModel(destination)
    }
    return destination
}
