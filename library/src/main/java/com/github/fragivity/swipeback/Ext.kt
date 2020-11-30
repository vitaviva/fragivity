@file:JvmName("Fragivity")

package com.github.fragivity.swipeback

import androidx.fragment.app.Fragment
import androidx.fragment.app.ReportFragment

/**
 * Detail config for swipe back
 */
val Fragment.swipeBackLayout
    get() = (parentFragment as ReportFragment)._swipeBackLayout


/**
 * Global config for enabling swipe back
 */
var enableSwipeBack = false