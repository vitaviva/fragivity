package com.github.fragivity.dialog

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.*
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.DialogFragmentNavigatorDestinationBuilder
import kotlin.reflect.KClass

/**
 * Shows dialog of [clazz] by pushing the dialogFragment to back stack
 */
fun NavHost.showDialog(
    clazz: KClass<out DialogFragment>,
    args: Bundle? = null
) = with(navController) {
    val node = putDialog(clazz)
    navigate(
        node.id, args,
        NavOptions.Builder().build()
    )
}


private fun NavController.putDialog(clazz: KClass<out DialogFragment>): DialogFragmentNavigator.Destination {
    val destId = clazz.hashCode()
    lateinit var destination: DialogFragmentNavigator.Destination
    if (graph.findNode(destId) == null) {
        destination = DialogFragmentNavigatorDestinationBuilder(
            navigatorProvider[DialogFragmentNavigator::class],
            destId,
            clazz
        ).apply {
            label = clazz.qualifiedName
        }.build()
        graph.plusAssign(destination)
    } else {
        destination = graph.findNode(destId) as DialogFragmentNavigator.Destination
    }
    return destination
}


