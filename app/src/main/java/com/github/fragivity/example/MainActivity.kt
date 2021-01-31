package com.github.fragivity.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.showDebugView
import com.github.fragivity.deeplink.handleDeepLink
import com.github.fragivity.loadRoot
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FragmentManager.enableDebugLogging(true)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host) as NavHostFragment

        navHostFragment.loadRoot(HomeFragment::class)
//        //you also can loadRoot with a factory
//        navHostFragment.loadRoot { HomeFragment() }

        navHostFragment.handleDeepLink(intent)
        navHostFragment.showDebugView(container)

    }
}
