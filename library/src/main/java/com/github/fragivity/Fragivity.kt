package com.github.fragivity

import android.os.Bundle
import androidx.arch.core.util.Function
import androidx.core.util.Supplier
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentProviderMap
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.github.fragivity.dialog.showDialog
import com.github.fragivity.router.NamedNavArgument
import com.github.fragivity.router.composable
import com.github.fragivity.router.popTo
import com.github.fragivity.router.push

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
        fragmentClazz: Class<T>,
        factory: Supplier<T>
    ) {
        val kClass = fragmentClazz.kotlin
        FragmentProviderMap[requireNotNull(kClass.qualifiedName)] = { factory.get() }
        navHost.loadRoot(kClass)
    }

    @JvmStatic
    fun composable(
        navHostFragment: NavHostFragment,
        route: String,
        factory: Function<Bundle, Fragment>
    ) {
        navHostFragment.composable(route, emptyList()) { factory.apply(it) }
    }

    @JvmStatic
    fun composable(
        navHostFragment: NavHostFragment,
        route: String,
        argument: NamedNavArgument,
        factory: Function<Bundle, Fragment>
    ) {
        navHostFragment.composable(route, listOf(argument)) { factory.apply(it) }
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
            _fragment.navigator.pushInternal(fragmentClazz.kotlin, navOptions)
        }

        @JvmOverloads
        fun <T : Fragment> push(
            fragmentClazz: Class<T>,
            factory: Supplier<T>,
            navOptions: NavOptions? = null
        ) {
            FragmentProviderMap[requireNotNull(fragmentClazz.kotlin.qualifiedName)] =
                { factory.get() }
            push(fragmentClazz, navOptions)
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