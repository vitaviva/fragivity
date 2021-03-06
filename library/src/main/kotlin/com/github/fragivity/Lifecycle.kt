package com.github.fragivity

import android.os.Parcelable
import android.view.View
import androidx.fragment.app.FragivityFragmentDestination
import androidx.fragment.app.Fragment
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.findNavController
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import kotlinx.parcelize.Parcelize

val Fragment.navigator: FragivityNavHost
    get() = findNavController().navigator

val View.navigator: FragivityNavHost
    get() = findNavController().navigator

val NavController.navigator: FragivityNavHost
    get() = fragivityHostViewModel.navHost

internal val NavController.fragivityHostViewModel: FragivityHostViewModel
    get() = ViewModelProvider(getViewModelStoreOwner(graph.id))
        .get(FragivityHostViewModel::class.java)

class FragivityHostViewModel : ViewModel() {

    lateinit var navHost: FragivityNavHost

    internal fun setUpNavHost(viewModel: FragivityNodeViewModel, navController: NavController) {
        navHost = FragivityNavHost(viewModel) { navController }
    }
}

class FragivityNodeViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    internal fun restoreDestination(navController: NavController, graphBuilder: NavGraphBuilder) {
        navDestSequence()
            .mapNotNull { savedStateHandle.get<NavDestinationBundle>(it) }
            .map { it.toDestination(navController) }
            .forEach { graphBuilder.addDestination(it) }
    }

    fun putDestination(node: NavDestination) {
        val key = NAV_DEST_PREFIX + node.id
        if (!savedStateHandle.contains(key)) {
            savedStateHandle.set(key, node.toBundle())
        }
    }

    fun removeDestination(id: Int) {
        val key = NAV_DEST_PREFIX + id
        if (savedStateHandle.contains(key)) {
            savedStateHandle.remove<NavDestinationBundle>(key)
        }
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
    }
}

private fun NavDestinationBundle.toDestination(navController: NavController): NavDestination {
    if (className.isNotEmpty()) {
        @Suppress("UNCHECKED_CAST")
        val clazz = Class.forName(className) as Class<Fragment>
        return navController.createNavDestination(id, clazz.kotlin)
    }
    return navController.createNavDestination(id)
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
