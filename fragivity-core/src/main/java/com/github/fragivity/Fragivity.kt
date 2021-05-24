package com.github.fragivity

import android.os.Bundle
import androidx.arch.core.util.Function
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.github.fragivity.dialog.showDialog

object Fragivity {

    @JvmStatic
    fun <T : Fragment> loadRoot(
        navHost: NavHostFragment,
        fragmentClazz: Class<T>
    ) {
        navHost.loadRoot(fragmentClazz.kotlin)
    }

    @JvmStatic
    fun <T : Fragment> loadRoot(
        navHost: NavHostFragment,
        route: String,
        fragmentClazz: Class<T>
    ) {
        navHost.loadRoot(route, fragmentClazz.kotlin)
    }

    @JvmStatic
    fun <T : Fragment> loadRoot(
        navHost: NavHostFragment,
        fragmentClazz: Class<T>,
        factory: Function<Bundle, T>
    ) {
        navHost.loadRoot(fragmentClazz.kotlin) { factory.apply(it) }
    }

    @JvmStatic
    fun <T : Fragment> loadRoot(
        navHost: NavHostFragment,
        route: String,
        fragmentClazz: Class<T>,
        factory: Function<Bundle, T>
    ) {
        navHost.loadRoot(route, fragmentClazz.kotlin) { factory.apply(it) }
    }

    @JvmStatic
    fun composable(
        navHostFragment: NavHostFragment,
        route: String,
        factory: Function<Bundle, Fragment>
    ) {
        navHostFragment.composable(route) { factory.apply(it) }
    }

    @JvmStatic
    fun composable(
        navHostFragment: NavHostFragment,
        route: String,
        argument: NamedNavArgument,
        factory: Function<Bundle, Fragment>
    ) {
        navHostFragment.composable(route, argument) { factory.apply(it) }
    }

    @JvmStatic
    fun composable(
        navHostFragment: NavHostFragment,
        route: String,
        arguments: List<NamedNavArgument>,
        factory: Function<Bundle, Fragment>
    ) {
        navHostFragment.composable(route, arguments) { factory.apply(it) }
    }

    @JvmStatic
    fun of(fragment: Fragment): Navigator {
        return Navigator(fragment)
    }

    class Navigator(val _fragment: Fragment) {

        @JvmOverloads
        fun <T : Fragment> push(
            fragmentClazz: Class<T>,
            navOptions: NavOptions? = null
        ) {
            _fragment.navigator.push(fragmentClazz.kotlin, navOptions)
        }

        @JvmOverloads
        fun <T : Fragment> push(
            fragmentClazz: Class<T>,
            factory: Function<Bundle, T>,
            navOptions: NavOptions? = null
        ) {
            _fragment.navigator.push(fragmentClazz.kotlin, navOptions) { factory.apply(it) }
        }

        @JvmOverloads
        fun <T : DialogFragment> showDialog(
            fragmentClazz: Class<T>,
            args: Bundle? = null
        ) {
            _fragment.navigator.showDialog(requireNotNull(fragmentClazz.kotlin), args)
        }

        @JvmOverloads
        fun push(route: String, navOptions: NavOptions? = null) {
            _fragment.navigator.push(route, navOptions)
        }

        fun pop(): Boolean {
            return _fragment.navigator.pop()
        }

        @JvmOverloads
        fun popTo(route: String, inclusive: Boolean = false): Boolean {
            return _fragment.navigator.popTo(route, inclusive)
        }

        companion object {
            @JvmStatic
            fun navOptionsBuilder(): NavOptionsBuilder {
                return NavOptionsBuilder()
            }
        }
    }
}