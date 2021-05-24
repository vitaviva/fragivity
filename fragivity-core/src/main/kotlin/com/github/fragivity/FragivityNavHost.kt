package com.github.fragivity

import androidx.navigation.NavDestination
import androidx.navigation.NavHost

/**
 * NavHost with viewModel to store NavDestination
 */
class FragivityNavHost(
    private val viewModel: FragivityNodeViewModel,
    navHost: NavHost
) : NavHost by navHost {

    @JvmSynthetic
    internal fun saveToViewModel(destination: NavDestination) {
        viewModel.putDestination(destination)
    }

    @JvmSynthetic
    internal fun removeFromViewModel(id: Int) {
        viewModel.removeDestination(id)
    }
}
