@file:JvmName("FragivityUtils")

package com.github.fragivity

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.use

/**
 * string hash容易冲突，使用System.identityHashCode离散hash，同时确保结果 > 0
 */
internal inline val Any.positiveHashCode: Int
    get() = System.identityHashCode(this) and Int.MAX_VALUE

internal fun View.appendBackground() {
    background = context.defaultBackground()
}

internal fun Context.defaultBackground(): Drawable? {
    return theme.obtainStyledAttributes(intArrayOf(android.R.attr.windowBackground)).use {
        val background = it.getResourceId(0, 0)
        ContextCompat.getDrawable(this, background)
    }
}
