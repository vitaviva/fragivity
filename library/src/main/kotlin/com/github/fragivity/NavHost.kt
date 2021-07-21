package com.github.fragivity

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

/**
 * NavHost with viewModel to store NavDestination
 */
sealed interface FragivityNavHost : NodeSaver {
    val navController: NavController
    val nodeSaver: NodeSaver
    fun onCleared()
}

class FragivityHostViewModel : ViewModel(), FragivityNavHost {

    private var _nodeSaver: NodeSaver? = null
    private var _navController: NavController? = null

    override val navController: NavController
        get() = _navController!!

    override val nodeSaver: NodeSaver
        get() = _nodeSaver!!

    @JvmSynthetic
    internal fun setupNodeSaver(nodeSaver: NodeSaver, navController: NavController) {
        _nodeSaver = nodeSaver
        _navController = navController
    }

    override fun addNode(node: NavDestination) {
        _nodeSaver?.addNode(node)
    }

    override fun removeNode(node: NavDestination) {
        _nodeSaver?.removeNode(node)
    }

    override fun setStartNode(nodeId: Int) {
        _nodeSaver?.setStartNode(nodeId)
    }

    override fun onCleared() {
        super.onCleared()
        _nodeSaver = null
        _navController = null
    }
}

val Fragment.navigator: FragivityNavHost
    get() = findNavController().navigator

val View.navigator: FragivityNavHost
    get() = findNavController().navigator

val NavController.navigator: FragivityNavHost
    get() = viewModel

private val NavController.viewModel: FragivityHostViewModel
    get() = ViewModelProvider(getViewModelStoreOwner(graph.id))
        .get(FragivityHostViewModel::class.java)

@JvmSynthetic
fun NavController.setupNodeSaver(nodeSaver: NodeSaver) {
    viewModel.setupNodeSaver(nodeSaver, this)
}