package com.github.fragivity

import android.os.Bundle
import androidx.core.util.Supplier
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentProviderMap
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
        fragmentClazz: Class<T>,
        factory: Supplier<T>
    ) {
        val kClass = fragmentClazz.kotlin
        FragmentProviderMap[requireNotNull(kClass.qualifiedName)] = { factory.get() }
        navHost.loadRoot(kClass)
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

        fun pop() {
            _fragment.navigator.pop()
        }

        companion object {
            @JvmStatic
            fun navOptionsBuilder(): NavOptionsBuilder {
                return NavOptionsBuilder()
            }
        }

    }
}