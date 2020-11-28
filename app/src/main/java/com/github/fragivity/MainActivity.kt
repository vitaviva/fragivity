package com.github.fragivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.showDebugView
import com.github.fragivity.deeplink.handleDeepLink
import com.my.example.R
import kotlinx.android.synthetic.main.freenav_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.freenav_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host) as NavHostFragment

        navHostFragment.loadRoot(HomeFragment::class, R.id.nav_host)
        navHostFragment.handleDeepLink(intent)
        navHostFragment.showDebugView(container)


    }
}
