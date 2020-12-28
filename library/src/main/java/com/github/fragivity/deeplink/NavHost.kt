@file:JvmName("NavHostUtil")

package com.github.fragivity.deeplink

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.os.Handler
import androidx.annotation.MainThread
import androidx.navigation.NavHost
import androidx.navigation.fragment.NavHostFragment
import com.github.fragivity.MyNavHost
import com.github.fragivity.putFragment

private val _isRouteInit by lazy(LazyThreadSafetyMode.NONE) {
    val c = Class.forName("com.github.fragivity._RouteLoaderKt")
    c.getDeclaredMethod("initRoute").invoke(null)
    true
}

@MainThread
fun NavHostFragment.handleDeepLink(intent: Intent) {

    with(intent) {
        if (ACTION_VIEW == action && _isRouteInit) {
            getFragmentInfo(data.toString())?.let {
                navController.putFragment(requireActivity(), it)
                //post用来确保HomeFragment已进入fragmentManager，才能获取并对其进行hide
                Handler().post { navController.navigate(data!!) }
            }
        }
    }
}