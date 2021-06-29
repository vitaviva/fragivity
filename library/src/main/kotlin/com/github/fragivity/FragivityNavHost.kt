package com.github.fragivity

import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHost

/**
 * NavHost with viewModel to store NavDestination
 */
class FragivityNavHost(
    private val viewModel: FragivityNodeViewModel,
    private val navController: NavController
) : NavHost {

    override fun getNavController(): NavController {
        return navController
    }

    @JvmSynthetic
    internal fun saveDestination(node: NavDestination) {
        viewModel.saveDestination(node)
    }

    @JvmSynthetic
    internal fun removeDestination(id: Int) {
        viewModel.removeDestination(id)
    }
}
