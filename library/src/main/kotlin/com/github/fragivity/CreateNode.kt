package com.github.fragivity

import android.os.Bundle
import androidx.fragment.app.FragivityFragmentDestination
import androidx.fragment.app.FragivityFragmentNavigator
import androidx.fragment.app.Fragment
import androidx.navigation.*
import androidx.navigation.fragment.FragmentNavigator
import com.github.fragivity.deeplink.getRouteUri
import kotlin.reflect.KClass

@JvmSynthetic
internal fun NavController.createNode(
    nodeId: Int, clazz: KClass<out Fragment>
) = FragmentNavigator.Destination(
    navigatorProvider[FragivityFragmentNavigator::class]
).apply {
    id = nodeId
    className = clazz.java.name
    label = clazz.qualifiedName
    getRouteUri(clazz)?.let {
        addDeepLink(it)
    }
}

@JvmSynthetic
internal fun NavController.createNode(
    nodeId: Int, block: ((Bundle) -> Fragment)? = null
) = FragivityFragmentDestination(
    navigatorProvider[FragivityFragmentNavigator::class]
).apply {
    id = nodeId
    this.factory = block
}

private fun NavController.createNode(
    nodeId: Int,
    clazz: KClass<out Fragment>,
    block: ((Bundle) -> Fragment)? = null
) = if (block != null) {
    createNode(nodeId, block)
} else {
    createNode(nodeId, clazz)
}

@JvmSynthetic
internal fun FragivityNavHost.getOrCreateNode(
    clazz: KClass<out Fragment>,
    block: ((Bundle) -> Fragment)? = null
): FragmentNavigator.Destination {
    val nodeId = clazz.positiveHashCode

    val graph = navController.graph

    var node = graph.findNode(nodeId) as? FragmentNavigator.Destination
    if (node == null) {
        node = navController.createNode(nodeId, clazz, block)
        graph += node
        addNode(node)
        return node
    }

    // check destination is valid
    if (block != null) {
        if (node is FragivityFragmentDestination) {
            node.factory = block
            return node
        }
    } else {
        if (node !is FragivityFragmentDestination) {
            return node
        }
    }

    // rebuild destination
    graph -= node
    removeNode(node)

    node = navController.createNode(nodeId, clazz, block)
    graph += node
    addNode(node)
    return node
}