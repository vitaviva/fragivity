@file:JvmName("DialogUtil")

package com.github.fragivity.dialog

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.DialogFragmentNavigatorDestinationBuilder
import androidx.navigation.get
import androidx.navigation.plusAssign
import com.github.fragivity.MyNavHost
import kotlin.reflect.KClass

/**
 * Shows dialog of [clazz] by pushing the dialogFragment to back stack
 */
fun MyNavHost.showDialog(
    clazz: KClass<out DialogFragment>,
    args: Bundle? = null
) = with(navController) {
    val node = putDialog(clazz)
    navigate(node.id, args)
}


private fun MyNavHost.putDialog(
    clazz: KClass<out DialogFragment>
): DialogFragmentNavigator.Destination {
    val destId = clazz.hashCode()
    val graph = navController.graph
    var destination = graph.findNode(destId) as? DialogFragmentNavigator.Destination
    if (destination == null) {
        destination = DialogFragmentNavigatorDestinationBuilder(
            navController.navigatorProvider[DialogFragmentNavigator::class],
            destId,
            clazz
        ).apply {
            label = clazz.qualifiedName
        }.build()
        graph.plusAssign(destination)
        saveToViewModel(destination)
    }
    return destination
}


