package com.github.fragivity

import androidx.navigation.NavArgument
import androidx.navigation.NavArgumentBuilder
import androidx.navigation.NavDestinationDsl
import androidx.navigation.NavType

/**
 * Construct a named [NavArgument] by using the [navArgument] method.
 */
class NamedNavArgument internal constructor(
    private val name: String,
    private val argument: NavArgument
) {
    internal operator fun component1(): String = name
    internal operator fun component2(): NavArgument = argument
}

@NavDestinationDsl
fun navArgument(
    name: String,
    builder: NavArgumentBuilder.() -> Unit
): NamedNavArgument = NamedNavArgument(name, NavArgumentBuilder().apply(builder).build())

fun stringArgument(name: String) = navArgument(name) { type = NavType.StringType }
fun intArgument(name: String) = navArgument(name) { type = NavType.IntType }
fun intArrayArgument(name: String) = navArgument(name) { type = NavType.IntArrayType }
fun longArgument(name: String) = navArgument(name) { type = NavType.LongType }
fun longArrayArgument(name: String) = navArgument(name) { type = NavType.LongArrayType }
fun floatArgument(name: String) = navArgument(name) { type = NavType.FloatType }
fun floatArrayArgument(name: String) = navArgument(name) { type = NavType.FloatArrayType }
fun boolArgument(name: String) = navArgument(name) { type = NavType.BoolType }
fun boolArrayArgument(name: String) = navArgument(name) { type = NavType.BoolArrayType }
fun referenceArgument(name: String) = navArgument(name) { type = NavType.ReferenceType }