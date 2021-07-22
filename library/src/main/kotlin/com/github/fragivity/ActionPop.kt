@file:JvmName("FragivityUtil")
@file:JvmMultifileClass

package com.github.fragivity

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.popBackStack
import kotlin.reflect.KClass

/**
 * pop current fragment from back stack
 */
@Suppress("NOTHING_TO_INLINE")
inline fun NavController.pop(): Boolean {
    return popBackStack()
}

/**
 * Pop back stack to [clazz]
 */
@JvmSynthetic
fun NavController.popTo(clazz: KClass<out Fragment>, inclusive: Boolean = false): Boolean {
    return popBackStack(clazz.positiveHashCode, inclusive)
}

/**
 * Pop back stack to [route]
 * WARN: currentRoute should not same as route
 */
@JvmSynthetic
fun NavController.popTo(route: String, inclusive: Boolean = false): Boolean {
    return popBackStack(route.toRequest(), inclusive)
}

/**
 * Pop back to root
 */
@Suppress("NOTHING_TO_INLINE")
inline fun NavController.popToRoot(): Boolean {
    return popTo(DEFAULT_ROOT_ROUTE, false)
}