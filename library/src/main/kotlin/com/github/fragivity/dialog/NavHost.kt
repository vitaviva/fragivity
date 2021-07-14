@file:JvmName("DialogUtil")

package com.github.fragivity.dialog

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.DialogFragmentNavigatorDestinationBuilder
import androidx.navigation.get
import androidx.navigation.plusAssign
import com.github.fragivity.FragivityNavHost
import com.github.fragivity.createRoute
import kotlin.reflect.KClass

/**
 * Shows dialog of [clazz] by pushing the dialogFragment to back stack
 */
fun FragivityNavHost.showDialog(
    clazz: KClass<out DialogFragment>,
    args: Bundle? = null
) = with(navController) {
    val node = putDialog(clazz)
    navigate(node.id, args)
}

private fun FragivityNavHost.putDialog(
    clazz: KClass<out DialogFragment>
): DialogFragmentNavigator.Destination {
    val route = createRoute(clazz)
    val graph = navController.graph
    var destination = graph.findNode(route) as? DialogFragmentNavigator.Destination
    if (destination == null) {
        destination = DialogFragmentNavigatorDestinationBuilder(
            navController.navigatorProvider[DialogFragmentNavigator::class],
            route,
            clazz
        ).apply {
            label = clazz.qualifiedName
        }.build()
        graph.plusAssign(destination)
        saveDestination(destination)
    }
    return destination
}


