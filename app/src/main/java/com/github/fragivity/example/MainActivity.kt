package com.github.fragivity.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.proxyFragmentFactory
import com.github.fragivity.*
import com.github.fragivity.debug.showDebugView
import com.github.fragivity.deeplink.handleDeepLink

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        proxyFragmentFactory()
        super.onCreate(savedInstanceState)
        setContentView(fragmentContainerView(R.id.nav_host))

        @Suppress("DEPRECATION")
        FragmentManager.enableDebugLogging(true)

        setupDefaultFragmentBackground()

        val navHostFragment = findOrCreateNavHostFragment(R.id.nav_host)
        navHostFragment.loadRoot(SplashFragment::class)
//        //you also can loadRoot with a factory
//        navHostFragment.loadRoot { SplashFragment() }

        navHostFragment.handleDeepLink(intent)
        navHostFragment.showDebugView(this)

        with(navHostFragment) {
            composable("feed") { FeedFragment.newInstance() }
            composable("search?keyword={keyword}", stringArgument("keyword")) {
                SearchFragment.newInstance()
            }
        }
    }
}
