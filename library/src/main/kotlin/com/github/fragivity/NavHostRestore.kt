package com.github.fragivity

import android.os.Parcelable
import androidx.fragment.app.FragivityFragmentDestination
import androidx.fragment.app.Fragment
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.FragmentNavigator
import kotlinx.parcelize.Parcelize

sealed interface NodeSaver {
    fun addNode(node: NavDestination)
    fun removeNode(node: NavDestination)
    fun setStartNode(nodeId: Int)
}

class FragivityNodeViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), NodeSaver {

    val startNodeId: Int?
        get() = savedStateHandle.get<Int>(NAV_DEST_START_NODE)

    internal fun restoreDestination(navController: NavController, graphBuilder: NavGraphBuilder) {
        val startNodeId = startNodeId
        navDestSequence()
            .mapNotNull { savedStateHandle.get<NavDestinationBundle>(it) }
            .map { it.toDestination(navController) }
            .forEach {
                if (startNodeId == it.id) {
                    it.appendRootRoute()
                }
                graphBuilder.addDestination(it)
            }
    }

    override fun addNode(node: NavDestination) {
        val key = NAV_DEST_PREFIX + node.id
        savedStateHandle.set(key, node.toBundle())
    }

    override fun removeNode(node: NavDestination) {
        val key = NAV_DEST_PREFIX + node.id
        if (savedStateHandle.contains(key)) {
            savedStateHandle.remove<NavDestinationBundle>(key)
        }
    }

    override fun setStartNode(nodeId: Int) {
        savedStateHandle.set(NAV_DEST_START_NODE, nodeId)
    }

    override fun onCleared() {
        super.onCleared()
        navDestSequence().forEach {
            savedStateHandle.remove<NavDestinationBundle>(it)
        }
    }

    private fun navDestSequence(): Sequence<String> {
        return savedStateHandle.keys()
            .asSequence()
            .filter { it.startsWith(NAV_DEST_PREFIX) }
    }

    companion object {
        private const val NAV_DEST_PREFIX = "NavDestKey-"
        private const val NAV_DEST_START_NODE = "NavDestKey#StartNodeId"
    }
}

private fun NavDestinationBundle.toDestination(navController: NavController): NavDestination {
    if (className.isNotEmpty()) {
        @Suppress("UNCHECKED_CAST")
        val clazz = Class.forName(className) as Class<Fragment>
        return navController.createNode(id, clazz.kotlin)
    }
    return navController.createNode(id)
}

private fun NavDestination.toBundle(): NavDestinationBundle {
    val clazzName = when (this) {
        is FragivityFragmentDestination -> ""
        is FragmentNavigator.Destination -> className
        is DialogFragmentNavigator.Destination -> className
        else -> error("Invalid Destination")
    }
    return NavDestinationBundle(id, clazzName)
}

@Parcelize
private data class NavDestinationBundle(val id: Int, val className: String) : Parcelable
