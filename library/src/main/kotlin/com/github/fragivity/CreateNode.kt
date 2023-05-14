@file:JvmName("FragivityUtil")
@file:JvmMultifileClass

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
    setClassName(clazz.java.name)
    label = clazz.qualifiedName
    getRouteUri(clazz)?.let {
        addDeepLink(it)
    }
}

@JvmSynthetic
internal fun NavController.createNode(
    nodeId: Int, block: ((Bundle) -> Fragment)?,
    label: CharSequence?
) = FragivityFragmentDestination(
    navigatorProvider[FragivityFragmentNavigator::class]
).apply {
    id = nodeId
    this.factory = block
    this.label = label
}

@JvmSynthetic
internal fun NavController.createNode(
    nodeId: Int,
    clazz: KClass<out Fragment>,
    block: ((Bundle) -> Fragment)? = null,
    label: CharSequence? = clazz.qualifiedName
) = if (block != null) {
    createNode(nodeId, block, label)
} else {
    createNode(nodeId, clazz)
}

@JvmSynthetic
internal fun NavController.getOrCreateNode(
    clazz: KClass<out Fragment>,
    block: ((Bundle) -> Fragment)? = null
): FragmentNavigator.Destination {
    val graph = graph
    val nodeSaver = nodeSaver

    val nodeId = clazz.positiveHashCode
    var node = graph.findNode(nodeId) as? FragmentNavigator.Destination
    if (node == null) {
        node = createNode(nodeId, clazz, block)
        graph += node
        nodeSaver.addNode(node)
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
    nodeSaver.removeNode(node)

    node = createNode(nodeId, clazz, block)
    graph += node
    nodeSaver.addNode(node)
    return node
}
