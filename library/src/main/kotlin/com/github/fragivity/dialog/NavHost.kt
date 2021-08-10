@file:JvmName("DialogUtil")

package com.github.fragivity.dialog

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.DialogFragmentNavigatorDestinationBuilder
import androidx.navigation.get
import androidx.navigation.plusAssign
import com.github.fragivity.nodeSaver
import com.github.fragivity.positiveHashCode
import kotlin.reflect.KClass

/**
 * Shows dialog of [clazz] by pushing the dialogFragment to back stack
 */
fun NavController.showDialog(
    clazz: KClass<out DialogFragment>,
    args: Bundle? = null
) {
    val node = putDialog(clazz)
    navigate(node.id, args)
}

private fun NavController.putDialog(
    clazz: KClass<out DialogFragment>
): DialogFragmentNavigator.Destination {
    val graph = graph

    val destId = clazz.positiveHashCode
    var destination = graph.findNode(destId) as? DialogFragmentNavigator.Destination
    if (destination == null) {
        destination = DialogFragmentNavigatorDestinationBuilder(
            navigatorProvider[DialogFragmentNavigator::class],
            destId,
            clazz
        ).apply {
            label = clazz.qualifiedName
        }.build()
        graph += destination
        nodeSaver.addNode(destination)
    }

    return destination
}


