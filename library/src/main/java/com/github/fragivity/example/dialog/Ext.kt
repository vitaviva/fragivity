package com.github.fragivity.example.dialog

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.github.fragivity.putDialog
import com.github.fragivity.putFragment
import kotlin.reflect.KClass

/**
 * Shows dialog of [clazz] by pushing the dialogFragment to back stack
 */
fun Fragment.showDialog(
    clazz: KClass<out DialogFragment>,
    args: Bundle? = null
) = with(parentFragment!!.findNavController()) {
    putFragment(this@showDialog::class)
    val node = putDialog(clazz)
    navigate(
        node.id, args,
        NavOptions.Builder().build()
    )
}

