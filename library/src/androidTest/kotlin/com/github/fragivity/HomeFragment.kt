package com.github.fragivity

import androidx.fragment.app.Fragment

const val ARGUMENT = "hello fragivity"

class HomeFragment : Fragment() {
    fun testArgument() : Boolean {
        return arguments?.get(null) == ARGUMENT
    }

}