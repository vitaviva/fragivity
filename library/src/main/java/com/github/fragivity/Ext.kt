@file:JvmName("Fragivity")

package com.github.fragivity

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.ReportFragment
import androidx.navigation.NavHost
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

val Fragment.navigator
    get() = MyNavHost(requireContext(), NavHost {
        val clazz = this::class
        requireParentFragment().findNavController().apply {
            putFragment(requireActivity(), clazz) //make sure the fragment in back stack
        }
    })

val View.navigator
    get() = MyNavHost(context, NavHost { findNavController() })


internal fun Fragment.requireParentFragmentManager() =
    if (parentFragment is ReportFragment) requireParentFragment().parentFragmentManager else parentFragmentManager


fun Fragment.requirePreviousFragment(): Fragment? {
    val fragmentList: List<Fragment> = requireParentFragmentManager().fragments
    val index = fragmentList.indexOf(parentFragment)
    return if (index > 0) fragmentList[index - 1].childFragmentManager.fragments[0] else null
}

/**
 * finish Activity
 */
fun Fragment.finish() {
    requireActivity().finish()
}

