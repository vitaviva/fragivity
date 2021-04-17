package com.github.fragivity.example.base

import android.view.View

interface OnItemClickListener {
    fun onItemClick(view: View, item: Any, position: Int)
}