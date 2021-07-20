package com.github.fragivity

import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHost

/**
 * NavHost with viewModel to store NavDestination
 */
class FragivityNavHost(
    @JvmSynthetic
    internal var _viewModel: FragivityNodeViewModel?,
    private var _navController: NavController?
) : NavHost {

    val viewModel: FragivityNodeViewModel
        get() = _viewModel!!

    override fun getNavController(): NavController {
        return _navController!!
    }

    @JvmSynthetic
    internal fun addNode(node: NavDestination) {
        viewModel.addNode(node)
    }

    @JvmSynthetic
    internal fun removeNode(node: NavDestination) {
        viewModel.removeNode(node)
    }

    @JvmSynthetic
    internal fun onCleared() {
        _viewModel = null
        _navController = null
    }
}
