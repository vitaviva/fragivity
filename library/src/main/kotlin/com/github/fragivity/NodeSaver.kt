package com.github.fragivity

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

// @Deprecated
typealias FragivityNavHost = NavController

inline val Fragment.navigator: NavController
    get() = findNavController()

inline val View.navigator: NavController
    get() = findNavController()

val NavController.nodeSaver: NodeSaver
    get() = ViewModelProvider(getViewModelStoreOwner(graph.id))
        .get(NodeSaverImpl::class.java)

internal val NodeSaver.parent: RestoreNodeSaver?
    get() = (this as NodeSaverImpl).realNodeSaver

@JvmSynthetic
internal fun NodeSaver.onCleared() {
    (this as NodeSaverImpl).realNodeSaver = null
}

@JvmSynthetic
internal fun NavController.bridge(parent: RestoreNodeSaver) {
    (nodeSaver as NodeSaverImpl).realNodeSaver = parent
}

sealed interface NodeSaver {
    fun addNode(node: NavDestination)
    fun removeNode(node: NavDestination)
    fun setStartNode(nodeId: Int)
}

class NodeSaverImpl : ViewModel(), NodeSaver {

    internal var realNodeSaver: RestoreNodeSaver? = null

    override fun addNode(node: NavDestination) {
        realNodeSaver?.addNode(node)
    }

    override fun removeNode(node: NavDestination) {
        realNodeSaver?.removeNode(node)
    }

    override fun setStartNode(nodeId: Int) {
        realNodeSaver?.setStartNode(nodeId)
    }

    override fun onCleared() {
        super.onCleared()
        realNodeSaver = null
    }
}