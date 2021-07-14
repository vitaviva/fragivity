/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.fragivity.example.multiback

import android.os.Bundle
import android.view.View
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.github.fragivity.example.R
import com.github.fragivity.findOrCreateNavHostFragment
import com.github.fragivity.loadMultiRoot
import com.github.fragivity.matchDestination
import com.github.fragivity.pushTo
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.ref.WeakReference

/**
 * An activity that inflates a layout that has a [BottomNavigationView].
 */
class MultiBackFragment : Fragment(R.layout.multi_back_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment = findOrCreateNavHostFragment(R.id.nav_host_container)
        navHostFragment.loadMultiRoot(Title::class, Leaderboard::class, Register::class)

        val navController = navHostFragment.navController

        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.setOnItemSelectedListener { item ->
            navController.pushTo(getPosition(item.itemId))
        }

        val weakReference = WeakReference(bottomNavigationView)
        navController.addOnDestinationChangedListener(object :
            NavController.OnDestinationChangedListener {
            override fun onDestinationChanged(
                controller: NavController,
                destination: NavDestination,
                arguments: Bundle?
            ) {
                val navigationView = weakReference.get()
                if (navigationView == null) {
                    navController.removeOnDestinationChangedListener(this)
                    return
                }

                navigationView.menu.forEach { item ->
                    if (destination.matchDestination(getPosition(item.itemId))) {
                        item.isChecked = true
                    }
                }
            }
        })
    }

    private fun getPosition(itemId: Int) = when (itemId) {
        R.id.home -> 0
        R.id.list -> 1
        R.id.form -> 2
        else -> 0
    }

    companion object {
        fun newInstance() = MultiBackFragment()
    }
}
