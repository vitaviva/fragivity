@file:JvmName("NavHostUtil")

package com.github.fragivity

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.collection.valueIterator
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentProvider
import androidx.fragment.app.MyFragmentNavigator
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.*
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorDestinationBuilder
import androidx.navigation.fragment.NavHostFragment
import com.github.fragivity.deeplink.getRouteUri
import kotlin.collections.forEach
import kotlin.collections.set
import kotlin.reflect.KClass


class MyNavHost(
    @PublishedApi internal val context: Context, navHost: NavHost
) : NavHost by navHost


@PublishedApi
internal fun MyNavHost.requireActivity(): FragmentActivity =
    context as FragmentActivity


/**
 * Navigates to fragment of [clazz] by pushing it to back stack
 */
fun MyNavHost.push(
    clazz: KClass<out Fragment>,
    args: Bundle? = null,
    extras: Navigator.Extras? = null,
    optionsBuilder: NavOptions.() -> Unit = {}
) = with(navController) {
    val node = putFragment(requireActivity(), clazz)
    navigate(
        node.id, args,
        convertNavOptions(clazz, NavOptions().apply(optionsBuilder)),
        extras
    )
}


inline fun <reified T : Fragment> MyNavHost.push(
    noinline optionsBuilder: NavOptions.() -> Unit = {},
    noinline block: () -> T
) {

    val type = object : TypeToken<T>() {}.type
    val node = navController.putFragment(requireActivity(), (type as Class<out Fragment>).kotlin)

    FragmentProvider[type.name] = block

    navController.navigate(
        node.id, null,
        convertNavOptions(T::class, NavOptions().apply(optionsBuilder))
    )
}


/**
 * pop current fragment from back stack
 */
fun NavHost.pop() {
    navController.popBackStack()
}


/**
 * Pop back stack to [clazz]
 */
fun NavHost.popTo(clazz: KClass<out Fragment>) {
    navController.popBackStack(clazz.hashCode(), false)
}


/**
 * load root fragment
 */
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
                })

        }.also { graph ->
            //for NavController#mBackStackToRestore
            val vm = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
            vm.nodes.valueIterator().forEach {
                it.removeFromParent()
                graph += it
            }
        }

    }
}


@PublishedApi
internal fun NavController.putFragment(
    activity: FragmentActivity,
    clazz: KClass<out Fragment>
): FragmentNavigator.Destination {
    val destId = clazz.hashCode()
    lateinit var destination: FragmentNavigator.Destination
    if (graph.findNode(destId) == null) {
        destination = (FragmentNavigatorDestinationBuilder(
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
        graph.plusAssign(destination)
        activity.saveToViewModel(destination)
    } else {
        destination = graph.findNode(destId) as FragmentNavigator.Destination
    }
    return destination
}
