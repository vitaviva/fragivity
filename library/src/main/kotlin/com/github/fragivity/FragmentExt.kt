@file:JvmName("FragivityUtil")
@file:JvmMultifileClass

package com.github.fragivity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.ReportFragment

internal fun Fragment.requireParentFragmentManager(): FragmentManager {
    return if (parentFragment is ReportFragment) {
        requireParentFragment().parentFragmentManager
    } else parentFragmentManager
}

internal fun Fragment.requirePreviousFragment(): Fragment? {
    if (this is ReportFragment) return parentFragmentManager.fragments.firstOrNull()
    val fragmentList: List<Fragment> = requireParentFragmentManager().fragments
    val index = fragmentList.indexOf(parentFragment)
    return if (index > 0) fragmentList[index - 1].childFragmentManager.fragments[0] else null
}
