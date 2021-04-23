package com.github.fragivity

import android.os.Parcelable
import android.view.View
import androidx.collection.SparseArrayCompat
import androidx.collection.valueIterator
import androidx.fragment.app.FragivityFragmentDestination
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.*
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import kotlinx.parcelize.Parcelize

internal val Fragment.fragivityHostViewModel: FragivityHostViewModel
    get() = if (this is NavHostFragment) {
        navController
    } else {
        requireParentFragment().findNavController()
    }.fragivityHostViewModel

internal val View.fragivityHostViewModel: FragivityHostViewModel
    get() = findNavController().fragivityHostViewModel

internal val NavController.fragivityHostViewModel: FragivityHostViewModel
    get() = ViewModelProvider(getViewModelStoreOwner(graph.id))
        .get(FragivityHostViewModel::class.java)

class FragivityHostViewModel : ViewModel() {

    lateinit var navHost: FragivityNavHost

    internal fun setUpNavHost(viewModel: FragivityNodeViewModel, navController: NavController) {
        navHost = FragivityNavHost(viewModel, NavHost { navController })
    }
}

class FragivityNodeViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private lateinit var nodes: SparseArrayCompat<NavDestination>

    internal fun restoreDestination(navController: NavController, graphBuilder: NavGraphBuilder) {
        val array = savedStateHandle.get(NAV_DEST_NODES_KEY) as? Array<NavDestinationBundle>?
        nodes = if (array != null) {
            SparseArrayCompat<NavDestination>(array.size).apply {
                array.forEach {
                    val destination = it.toDestination(navController)
                    put(it.id, destination)

                    graphBuilder.addDestination(destination)
                }
            }
        } else {
            SparseArrayCompat()
        }
    }

    fun putDestination(node: NavDestination) {
        if (nodes.containsKey(node.id)) return
        nodes.put(node.id, node)
        saveNodesInState()
    }

    fun removeDestination(id: Int) {
        if (!nodes.containsKey(id)) return
        nodes.remove(id)
        saveNodesInState()
    }

    private fun saveNodesInState() {
        val array = Array<NavDestinationBundle?>(nodes.size()) { null }
        var i = 0
        nodes.valueIterator().forEach {
            array[i++] = it.toBundle()
        }
        savedStateHandle.set(NAV_DEST_NODES_KEY, array)
    }

    override fun onCleared() {
        super.onCleared()
        nodes.clear()
    }

    companion object {
        private const val NAV_DEST_NODES_KEY = "NavDestKey"
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
private data class NavDestinationBundle(
    val id: Int,
    val className: String
) : Parcelable
