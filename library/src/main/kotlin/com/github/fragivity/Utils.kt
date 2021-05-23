@file:JvmName("FragivityUtils")

package com.github.fragivity

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.use
import androidx.fragment.app.Fragment

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

internal operator fun Bundle?.plus(optionArgs: Bundle?): Bundle? {
    if (optionArgs == null) return this
    if (this == null) return optionArgs
    return Bundle().apply {
        putAll(optionArgs)
        putAll(this@plus)
    }
}

internal operator fun Fragment.plusAssign(newBundle: Bundle?) {
    if (newBundle == null) {
        return
    }

    val oldArgs: Bundle? = this.arguments
    if (oldArgs == null) {
        this.arguments = newBundle
        return
    }

    oldArgs.putAll(newBundle)
}

internal fun ArrayDeque<Int>.replaceAll(array: IntArray?) {
    if (array == null) return
    clear()
    for (value in array) {
        add(value)
    }
}