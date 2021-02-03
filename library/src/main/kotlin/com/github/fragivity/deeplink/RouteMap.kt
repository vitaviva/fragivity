package com.github.fragivity.deeplink

import androidx.fragment.app.Fragment
import kotlin.reflect.KClass

private val _routeMap = mutableMapOf<String, KClass<out Fragment>>()

internal fun getRouteUri(clazz: KClass<out Fragment>): String? =
    _routeMap.toList().firstOrNull { it.second == clazz }?.first


internal fun getFragmentInfo(uri: String): KClass<out Fragment>? =
    _routeMap[uri]


/**
 *  Add URI info for Fragment
 */
fun addRoute(uriStr: String, clazz: KClass<out Fragment>) {
    check(_routeMap.getOrPut(uriStr) { clazz } == clazz) {
        """Deep links "$uriStr" duplicated !!"""
    }
}