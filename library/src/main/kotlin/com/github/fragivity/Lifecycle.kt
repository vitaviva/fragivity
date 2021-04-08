package com.github.fragivity

import android.os.Parcelable
import android.view.View
import androidx.activity.ComponentActivity
import androidx.collection.SparseArrayCompat
import androidx.collection.valueIterator
import androidx.fragment.app.Fragment
import androidx.fragment.app.MyFragmentNavigator
import androidx.lifecycle.*
import androidx.navigation.*
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.parcel.Parcelize
import kotlin.collections.set

internal fun ComponentActivity.getFragivityViewModel() =
    ViewModelProvider(this, defaultViewModelProviderFactory)
        .get(FragivityViewModel::class.java)

class FragivityViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val fragNavHostMap = mutableMapOf<Int, MyNavHost>()
    private val viewNavHostMap = mutableMapOf<Int, MyNavHost>()

    private var _navController: NavController? = null
    private val navController get() = _navController!!

    private val nodes by lazy {
        val array: Array<NavDestinationBundle> = savedStateHandle.get(NAV_DEST_NODES_KEY)
            ?: return@lazy SparseArrayCompat<NavDestination>()

        SparseArrayCompat<NavDestination>(array.size).apply {
            array.forEach { put(it.id, it.toDestination(navController)) }
        }
    }

    internal fun restoreDestination(graphBuilder: NavGraphBuilder) {
        nodes.valueIterator().forEach {
            it.removeFromParent()
            graphBuilder.addDestination(it)
        }
    }

    internal fun setNavController(navController: NavController) {
        _navController = navController
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

    fun getNavHost(fragment: Fragment): MyNavHost {
        val id = fragment.navHostId
        var navHost = fragNavHostMap[id]
        if (navHost != null) return navHost

        navHost = MyNavHost(this, NavHost {
            if (fragment is NavHostFragment) {
                fragment.navController
            } else {
                fragment.requireParentFragment().findNavController()
            }
        })
        navHost.putFragment(fragment::class)

        val lifecycleOwner = if (fragment.view != null) fragment.viewLifecycleOwner else fragment
        lifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (Lifecycle.Event.ON_DESTROY == event) {
                fragNavHostMap.remove(id)
            }
        })

        fragNavHostMap[id] = navHost
        return navHost
    }

    fun getNavHost(view: View): MyNavHost {
        val id = view.navHostId
        var navHost = viewNavHostMap[id]
        if (navHost != null) return navHost

        navHost = MyNavHost(this, NavHost {
            view.findNavController()
        })

        view.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View?) {}

            override fun onViewDetachedFromWindow(v: View?) {
                viewNavHostMap.remove(id)
            }
        })

        viewNavHostMap[id] = navHost
        return navHost
    }

    override fun onCleared() {
        super.onCleared()
        fragNavHostMap.clear()
        viewNavHostMap.clear()
        nodes.clear()
        _navController = null
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
    return navController.createMyNavDestination(id)
}

private fun NavDestination.toBundle(): NavDestinationBundle {
    val clazzName = when (this) {
        is MyFragmentNavigator.MyDestination -> ""
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

private inline val Any.navHostId: Int
    get() = System.identityHashCode(this)