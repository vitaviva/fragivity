@file:JvmName("NavHostUtil")

package com.github.fragivity

import android.os.Bundle
import androidx.fragment.app.FragivityNavigator
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentProvider
import androidx.navigation.*
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorDestinationBuilder
import androidx.navigation.fragment.NavHostFragment
import com.github.fragivity.deeplink.getRouteUri
import kotlin.reflect.KClass


/**
 * Navigates to fragment of [clazz] by pushing it to back stack
 */
fun NavHost.push(
    clazz: KClass<out Fragment>,
    args: Bundle? = null,
    extras: Navigator.Extras? = null,
    optionsBuilder: NavOptions.() -> Unit = {}
) = with(navController) {
    val node = putFragment(clazz)
    navigate(
        node.id, args,
        convertNavOptions(clazz, NavOptions().apply(optionsBuilder)),
        extras
    )
}


inline fun <reified T : Fragment> NavHost.push(
    noinline optionsBuilder: NavOptions.() -> Unit = {},
    noinline block: () -> T
) {

    val type = object : TypeToken<T>() {}.type
    val node = navController.putFragment((type as Class<out Fragment>).kotlin)

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
            FragivityNavigator(
                context,
                childFragmentManager,
                id
            )
        )
        val startDestId = root.hashCode()
        graph = createGraph(startDestination = startDestId) {

            destination(
                FragmentNavigatorDestinationBuilder(
                    provider[FragivityNavigator::class],
                    startDestId,
                    root
                ).apply {
                    label = "home"
                })

        }

    }
}


@PublishedApi
internal fun NavController.putFragment(clazz: KClass<out Fragment>): FragmentNavigator.Destination {
    val destId = clazz.hashCode()
    lateinit var destination: FragmentNavigator.Destination
    if (graph.findNode(destId) == null) {
        destination = (FragmentNavigatorDestinationBuilder(
            navigatorProvider[FragivityNavigator::class],
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
    } else {
        destination = graph.findNode(destId) as FragmentNavigator.Destination
    }
    return destination
}
