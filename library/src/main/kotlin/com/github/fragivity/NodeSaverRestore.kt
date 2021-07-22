package com.github.fragivity

import android.os.Parcelable
import androidx.fragment.app.FragivityFragmentDestination
import androidx.fragment.app.Fragment
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import kotlinx.parcelize.Parcelize

val NavHostFragment.nodeSaver: RestoreNodeSaver
    get() = ViewModelProvider(this, defaultViewModelProviderFactory)
        .get(RestoreNodeSaverImpl::class.java)

internal val RestoreNodeSaver.startNodeId: Int?
    get() = (this as RestoreNodeSaverImpl).startNodeId

@JvmSynthetic
internal fun RestoreNodeSaver.restoreNodes(
    navController: NavController,
    graphBuilder: NavGraphBuilder
) {
    (this as RestoreNodeSaverImpl).restoreNodes(navController, graphBuilder)
}

sealed interface RestoreNodeSaver : NodeSaver

class RestoreNodeSaverImpl(
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), RestoreNodeSaver {

    internal val startNodeId: Int?
        get() = savedStateHandle.get<Int>(NAV_DEST_START_NODE)

    private val navDestSequence: Sequence<String>
        get() = savedStateHandle.keys().asSequence()
            .filter { it.startsWith(NAV_DEST_PREFIX) }

    internal fun restoreNodes(navController: NavController, graphBuilder: NavGraphBuilder) {
        navDestSequence
            .mapNotNull { savedStateHandle.get<NavDestinationBundle>(it) }
            .map { it.toDestination(navController) }
            .forEach { graphBuilder.addDestination(it) }
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
        navDestSequence.forEach {
            savedStateHandle.remove<NavDestinationBundle>(it)
        }
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
    return navController.createNode(id, null, label)
}

private fun NavDestination.toBundle(): NavDestinationBundle {
    val clazzName = when (this) {
        is FragivityFragmentDestination -> ""
        is FragmentNavigator.Destination -> className
        is DialogFragmentNavigator.Destination -> className
        else -> error("Invalid Destination")
    }
    return NavDestinationBundle(id, clazzName, label)
}

@Parcelize
private data class NavDestinationBundle(
    val id: Int,
    val className: String,
    val label: CharSequence?
) : Parcelable
