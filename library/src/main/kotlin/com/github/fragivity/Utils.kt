@file:JvmName("FragivityUtils")

package com.github.fragivity

import android.view.View
import androidx.core.content.res.use

// make hashCode > 0
internal inline val Any.positiveHashCode: Int
    get() = hashCode() and Int.MAX_VALUE

internal fun View.appendBackground() {
    context.theme.obtainStyledAttributes(intArrayOf(android.R.attr.windowBackground)).use {
        val background = it.getResourceId(0, 0)
        setBackgroundResource(background)
    }
}
