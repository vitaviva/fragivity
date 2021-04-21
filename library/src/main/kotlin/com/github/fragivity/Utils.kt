@file:JvmName("FragivityUtils")

package com.github.fragivity

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.use

// make hashCode > 0
internal inline val Any.positiveHashCode: Int
    get() = hashCode() and Int.MAX_VALUE

internal fun View.appendBackground() {
    background = context.defaultBackground()
}

internal fun Context.defaultBackground(): Drawable? {
    return theme.obtainStyledAttributes(intArrayOf(android.R.attr.windowBackground)).use {
        val background = it.getResourceId(0, 0)
        ContextCompat.getDrawable(this, background)
    }
}
