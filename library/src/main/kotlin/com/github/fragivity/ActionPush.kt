@file:JvmName("FragivityUtil")
@file:JvmMultifileClass

package com.github.fragivity

import android.os.Bundle
import androidx.fragment.app.FragivityFragmentNavigator
import androidx.fragment.app.Fragment
import androidx.navigation.*
import kotlin.reflect.KClass

@JvmSynthetic
fun NavController.push(route: String, optionsBuilder: NavOptions.() -> Unit = {}) {
    push(route, navOptions(optionsBuilder))
}

@JvmSynthetic
fun NavController.push(route: String, navOptions: NavOptions?) {
    val (node, matchingArgs) = findDestinationAndArgs(route.toRequest()) ?: return
    pushInternal(node, navOptions, matchingArgs)
}

/**
 * Navigates to fragment of [clazz] by pushing it to back stack
 */
@JvmSynthetic
fun NavController.push(
    clazz: KClass<out Fragment>,
    optionsBuilder: NavOptions.() -> Unit = {}
) {
    push(clazz, navOptions(optionsBuilder))
}

@JvmSynthetic
fun NavController.push(
    clazz: KClass<out Fragment>,
    navOptions: NavOptions?
) {
    pushInternal(getOrCreateNode(clazz), navOptions)
}

/**
 * Navigates to a fragment by its factory
 */
@JvmSynthetic
inline fun <reified T : Fragment> NavController.push(
    noinline optionsBuilder: NavOptions.() -> Unit = {},
    noinline block: (Bundle) -> T
) {
    push(T::class, navOptions(optionsBuilder), block)
}

@JvmSynthetic
fun NavController.push(
    clazz: KClass<out Fragment>,
    navOptions: NavOptions?,
    factory: (Bundle) -> Fragment
) {
    pushInternal(getOrCreateNode(clazz, factory), navOptions)
}

@JvmSynthetic
private fun NavController.pushInternal(
    node: NavDestination,
    navOptions: NavOptions?,
    matchingArgs: Bundle? = null
) {
    if (navOptions == null) {
        navigate(node.id)
        return
    }

    val nodeSaver = nodeSaver

    // fix https://github.com/vitaviva/fragivity/issues/31
    // popSelf为true时， mFragmentManager.mBackStack需要与NavController.mBackStack同步更新，
    //  mFragmentManager在Navigator中会处理，NavController需要在此处处理，先删除当前Destination
    if (navOptions.popSelf) {
        // 删除BackStack的同时也删除graph中的node
        val oldNode = removeLastBackStackEntry()?.destination

        // 如果rootNode被删除了，认为新push的node为rootNode
        if (isNullRootNode()) {
            // 删除旧的rootNode
            if (oldNode != null) {
                graph.remove(oldNode)
                nodeSaver.removeNode(oldNode)
            }

            node.appendRootRoute()

            // 记录开始id用于重建
            nodeSaver.setStartNode(node.id)
        }
    }

    when (navOptions.launchMode) {
        LaunchMode.STANDARD,
        LaunchMode.SINGLE_TOP -> {
            navigate(
                node.id,
                navOptions.toBundle() + matchingArgs,
                navOptions.totOptions(node.id),
                navOptions.totExtras()
            )
        }
        LaunchMode.SINGLE_TASK -> {
            // curr == target || try pop to target
            if (currentDestination?.id == node.id || popBackStack(node.id, false)) {
                val navigator: Navigator<out NavDestination> =
                    navigatorProvider.getNavigator(node.navigatorName)
                if (navigator is FragivityFragmentNavigator) {
                    navigator.restoreTopFragment(
                        node.id,
                        navOptions.toBundle() + matchingArgs
                    )
                }
                return
            }

            // create target
            navigate(
                node.id,
                navOptions.toBundle() + matchingArgs,
                navOptions.totOptions(node.id),
                navOptions.totExtras()
            )
        }
    }
}