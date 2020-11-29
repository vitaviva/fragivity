package com.github.fragivity.swipeback

import androidx.fragment.app.Fragment
import androidx.fragment.app.ReportFragment

val Fragment.swipeBackLayout
    get() = (parentFragment as ReportFragment)._swipeBackLayout

