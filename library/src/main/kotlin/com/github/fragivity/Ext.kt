@file:JvmName("FragivityUtil")
@file:JvmMultifileClass

package com.github.fragivity

import android.view.View
import androidx.collection.valueIterator
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentProviderMap
import androidx.fragment.app.MyFragmentNavigator
import androidx.fragment.app.ReportFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.*
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorDestinationBuilder
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.github.fragivity.deeplink.getRouteUri
import kotlin.reflect.KClass

private val _fragNavHostMap = mutableMapOf<Fragment, MyNavHost>()
private val _ViewNavHostMap = mutableMapOf<View, MyNavHost>()

val Fragment.navigator: MyNavHost
    get() {
        return _fragNavHostMap.getOrPut(this) {
            MyNavHost(requireContext(), NavHost {
                if (this is NavHostFragment) navController
                else requireParentFragment().findNavController()
            }).apply {//make sure the fragment in back stack
                putFragment(this@navigator::class)
            }.also {//clean when no longer needed
                val lifecycle = view?.run { viewLifecycleOwner.lifecycle } ?: this.lifecycle
                lifecycle.addObserver(LifecycleEventObserver { _, event ->
                    if (Lifecycle.Event.ON_DESTROY == event) {
                        _fragNavHostMap.remove(this@navigator)
                    }
                })
            }
        }
    }


val View.navigator: MyNavHost
    get() {
        return _ViewNavHostMap.getOrPut(this) {
            MyNavHost(context, NavHost { findNavController() })
                .also { //clean
                    this.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                        override fun onViewAttachedToWindow(v: View?) {
                        }

                        override fun onViewDetachedFromWindow(v: View?) {
                            _ViewNavHostMap.remove(this@navigator)
                        }
                    })
                }
        }
    }


/**
 * Load root fragment
 */
@JvmSynthetic
fun NavHostFragment.loadRoot(root: KClass<out Fragment>) {
    val context = activity ?: return

    navController.apply {
        navigatorProvider.addNavigator(
            MyFragmentNavigator(
                context,
                childFragmentManager,
                id
            )
        )
        val startDestId = root.hashCode()
        graph = createGraph(startDestination = startDestId) {

            destination(
                FragmentNavigatorDestinationBuilder(
                    provider[MyFragmentNavigator::class],
                    startDestId,
                    root
                ).apply {
                    label = "home"
                })

        }.also { graph ->
            //restore destination from vm for NavController#mBackStackToRestore
            val activity = requireActivity()
            val vm = ViewModelProvider(
                activity,
                SavedStateViewModelFactory(activity.application, activity)
            ).get(MyViewModel::class.java).also {
                it.navController = this
            }
            vm.nodes.valueIterator().forEach {
                it.removeFromParent()
                graph += it
            }
        }


    }
}

/**
 * Load root fragment by factory
 */
@JvmSynthetic
inline fun <reified T : Fragment> NavHostFragment.loadRoot(
    noinline block: () -> T
) {
    val clazz = T::class
    FragmentProviderMap[clazz.qualifiedName!!] = block
    loadRoot(T::class)
}

@JvmSynthetic
internal fun NavController.createNavDestination(
    destId: Int,
    clazz: KClass<out Fragment>
): FragmentNavigator.Destination {
    return (FragmentNavigatorDestinationBuilder(
        navigatorProvider[MyFragmentNavigator::class],
        destId,
        clazz
    ).apply {
        label = clazz.qualifiedName
        getRouteUri(clazz)?.let {
            deepLink {
                uriPattern = it
            }
        }
    }).build()
}

internal fun Fragment.requireParentFragmentManager() =
    if (parentFragment is ReportFragment) requireParentFragment().parentFragmentManager else parentFragmentManager


internal fun Fragment.requirePreviousFragment(): Fragment? {
    if (this is ReportFragment) return parentFragmentManager.fragments.firstOrNull()
    val fragmentList: List<Fragment> = requireParentFragmentManager().fragments
    val index = fragmentList.indexOf(parentFragment)
    return if (index > 0) fragmentList[index - 1].childFragmentManager.fragments[0] else null
}

/**
 * finish Activity
 */
fun Fragment.finish() {
    requireActivity().finish()
}

