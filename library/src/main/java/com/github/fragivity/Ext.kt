@file:JvmName("Fragivity")

package com.github.fragivity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.*
import androidx.navigation.*
import androidx.navigation.fragment.*
import com.github.fragivity.deeplink.getRouteUri
import kotlin.reflect.KClass


@PublishedApi
internal fun NavController.putFragment(clazz: KClass<out Fragment>): FragmentNavigator.Destination {
    val destId = clazz.hashCode()
    lateinit var destination: FragmentNavigator.Destination
    if (graph.findNode(destId) == null) {
        destination = (FragmentNavigatorDestinationBuilder(
            navigatorProvider[FragivityNavigator::class],
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
        graph.plusAssign(destination)
    } else {
        destination = graph.findNode(destId) as FragmentNavigator.Destination
    }
    return destination
}


internal fun NavController.putDialog(clazz: KClass<out DialogFragment>): DialogFragmentNavigator.Destination {
    val destId = clazz.hashCode()
    lateinit var destination: DialogFragmentNavigator.Destination
    if (graph.findNode(destId) == null) {
        destination = (DialogFragmentNavigatorDestinationBuilder(
            navigatorProvider[DialogFragmentNavigator::class],
            destId,
            clazz
        ).apply {
            label = clazz.qualifiedName
        }).build()
        graph.plusAssign(destination)
    } else {
        destination = graph.findNode(destId) as DialogFragmentNavigator.Destination
    }
    return destination
}

/**
 * Navigates to fragment of [clazz] by pushing it to back stack
 */
fun Fragment.push(
    clazz: KClass<out Fragment>,
    args: Bundle? = null,
    extras: Navigator.Extras? = null,
    optionsBuilder: NavOptions.() -> Unit = {}
) = with(requireParentFragment().findNavController()) {
    putFragment(this@push::class)
    val node = putFragment(clazz)
    navigate(
        node.id, args,
        convertNavOptions(clazz, NavOptions().apply(optionsBuilder)),
        extras
    )
}

fun View.push(
    clazz: KClass<out Fragment>,
    args: Bundle? = null,
    extras: Navigator.Extras? = null,
    optionsBuilder: NavOptions.() -> Unit = {}
) {
    val controller = this.findNavController()
    val node = controller.putFragment(clazz)

    controller.navigate(
        node.id, args,
        convertNavOptions(clazz, NavOptions().apply(optionsBuilder)),
        extras
    )

}

inline fun <reified T : Fragment> Fragment.push(
    noinline optionsBuilder: NavOptions.() -> Unit = {},
    noinline block: () -> T
) {

    val type = object : TypeToken<T>() {}.type
    val controller = requireParentFragment().findNavController()

    controller.putFragment(this::class)
    val node = controller.putFragment((type as Class<out Fragment>).kotlin)

    FragmentProvider[type.name] = block

    controller.navigate(
        node.id, null,
        convertNavOptions(T::class, NavOptions().apply(optionsBuilder))
    )
}


/**
 * pop current fragment from back stack
 */
fun Fragment.pop() {
    val controller = requireParentFragment().findNavController()
    controller.popBackStack()
}


/**
 * Pop back stack to [clazz]
 */
fun Fragment.popTo(clazz: KClass<out Fragment>) {
    val controller = requireParentFragment().findNavController()
    controller.popBackStack(clazz.hashCode(), false)
}


/**
 * finish Activity
 */
fun Fragment.finish() {
    requireActivity().finish()
}


/**
 * load root fragment
 */
fun NavHostFragment.loadRoot(clazz: KClass<out Fragment>, id: Int) {
    val context = activity ?: return

    navController.apply {
        navigatorProvider.addNavigator(
            FragivityNavigator(
                context,
                childFragmentManager,
                id
            )
        )
        val startDestId = clazz.hashCode()
        graph = createGraph(startDestination = startDestId) {

            destination(
                FragmentNavigatorDestinationBuilder(
                    provider[FragivityNavigator::class],
                    startDestId,
                    clazz
                ).apply {
                    label = "home"
//                argument(nav_graph.args.plant_id) {
//                    type = NavType.StringType
//                }
//                action(nav_graph.action.to_first) {
//                    destinationId = nav_graph.dest.first
//                }
                })

        }

    }
}


fun Fragment.requireParentFragmentManager() =
    if (parentFragment is ReportFragment) requireParentFragment().parentFragmentManager else parentFragmentManager


fun Fragment.requirePreviousFragment(): Fragment? {
    val fragmentList: List<Fragment> = requireParentFragmentManager().fragments
    val index = fragmentList.indexOf(parentFragment)
    return if (index > 0) fragmentList[index - 1].childFragmentManager.fragments[0] else null
}
