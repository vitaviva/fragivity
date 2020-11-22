package com.github.fragivity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentProvider
import androidx.fragment.app.MyFragmentNavigator
import androidx.navigation.*
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorDestinationBuilder
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import kotlin.reflect.KClass


//object nav_graph {
//
//    const val id = 1 // graph id
//
//    object dest {
//        const val home = 2
//        const val first = 3
//    }
//
//    object action {
//        const val to_first = 4
//    }
//
//    object args {
//        const val plant_id = "plantId"
//    }
//}

fun NavController.putFragment(clazz: KClass<out Fragment>): FragmentNavigator.Destination {
    val destId = clazz.hashCode()
    lateinit var destination: FragmentNavigator.Destination
    if (graph.findNode(destId) == null) {
        destination = (FragmentNavigatorDestinationBuilder(
            navigatorProvider[MyFragmentNavigator::class],
            destId,
            clazz
        ).apply {
            label = clazz.qualifiedName
//                argument(nav_graph.args.plant_id) {
//                    type = NavType.StringType
//                }
        }).build()
        graph.plusAssign(destination)
    } else {
        destination = graph.findNode(destId) as FragmentNavigator.Destination
    }
    return destination
}

/**
 * push a fragment to back stack
 */
fun Fragment.push(
    clazz: KClass<out Fragment>,
    args: Bundle? = null,
    extras: Navigator.Extras? = null,
    optionsBuilder: NavOptions.Builder.() -> Unit = {}
) {

    val controller = parentFragment!!.findNavController()

    controller.putFragment(this::class)
    val node = controller.putFragment(clazz)

    controller.navigate(
        node.id, args,
        NavOptions.Builder().apply(optionsBuilder).build()
    )
}

fun View.push(
    clazz: KClass<out Fragment>,
    args: Bundle? = null,
    extras: Navigator.Extras? = null,
    optionsBuilder: NavOptions.Builder.() -> Unit = {}
) {
    val controller = this.findNavController()

    val node = controller.putFragment(clazz)

    controller.navigate(
        node.id, args,
        NavOptions.Builder().apply(optionsBuilder).build(),
        extras
    )

}

inline fun <reified T : Fragment> Fragment.push(
    noinline optionsBuilder: NavOptions.Builder.() -> Unit = {},
    noinline block: () -> T
) {

    val type = object : TypeToken<T>() {}.type
    val controller = parentFragment!!.findNavController()

    controller.putFragment(this::class)
    val node = controller.putFragment((type as Class<out Fragment>).kotlin)

    FragmentProvider[type.name] = block

    controller.navigate(
        node.id, null,
        NavOptions.Builder().apply(optionsBuilder).build()
    )
}


/**
 * pop current fragment from back stack
 */
fun Fragment.pop() {
    val controller = parentFragment!!.findNavController()
    controller.popBackStack()
}


/**
 * load root fragment
 */
fun NavHostFragment.loadRoot(clazz: KClass<out Fragment>, id: Int) {
    val context = activity ?: return

    navController.apply {
        navigatorProvider.addNavigator(
            MyFragmentNavigator(
                context,
                childFragmentManager,
                id
            )
        )
        val startDestId = clazz.hashCode()
        graph = createGraph(startDestination = startDestId) {

            destination(
                FragmentNavigatorDestinationBuilder(
                    provider[MyFragmentNavigator::class],
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


//class GardenActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//
////        action(nav_graph.action.to_plant_detail) {
////            destinationId = nav_graph.dest.plant_detail
////            navOptions {
////                anim {
////                    enter = R.anim.nav_default_enter_anim
////                    exit = R.anim.nav_default_exit_anim
////                    popEnter = R.anim.nav_default_pop_enter_anim
////                    popExit = R.anim.nav_default_pop_exit_anim
////                }
////                popUpTo(nav_graph.dest.start_dest) {
////                    inclusive = true // default false
////                }
////                // if popping exclusively, you can specify popUpTo as
////                // a property. e.g. popUpTo = nav_graph.dest.start_dest
////                launchSingleTop = true // default false
////            }
////        }
//
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_garden)
//
//        val navHostFragment = supportFragmentManager
//            .findFragmentById(R.id.nav_host) as NavHostFragment
//
//        navHostFragment.navController.apply {
//            graph = createGraph(nav_graph.id, nav_graph.dest.home) {
//                fragment<HomeViewPagerFragment>(nav_graph.dest.home) {
//                    label = getString(R.string.home_title)
//                    action(to_plant_detail) {
//                        destinationId = nav_graph.dest.plant_detail
//                    }
//                }
//                fragment<PlantDetailFragment>(nav_graph.dest.plant_detail) {
//                    label = getString(R.string.plant_detail_title)
//                    argument(nav_graph.args.plant_id) {
//                        type = NavType.StringType
//                    }
//                }
//            }
//        }
//    }
//
//    private fun navigateToPlant(plantId: String) {
//
//        val args = bundleOf(nav_graph.args.plant_id to plantId)
//
//        findNavController().navigate(to_plant_detail, args)
//    }
//
//}


