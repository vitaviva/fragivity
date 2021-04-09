package com.github.fragivity.example.base

import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun TextView.setTextColorRes(@ColorRes colorId: Int) {
    setTextColor(ContextCompat.getColor(context, colorId))
}