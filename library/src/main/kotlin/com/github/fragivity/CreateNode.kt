package com.github.fragivity

import android.os.Bundle
import androidx.fragment.app.FragivityFragmentDestination
import androidx.fragment.app.FragivityFragmentNavigator
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.get
import com.github.fragivity.deeplink.getRouteUri
import kotlin.reflect.KClass

@JvmSynthetic
internal fun NavController.createNode(
    nodeId: Int, clazz: KClass<out Fragment>
) = FragmentNavigator.Destination(
    navigatorProvider[FragivityFragmentNavigator::class]
).apply {
    id = nodeId
    className = clazz.java.name
    label = clazz.qualifiedName
    getRouteUri(clazz)?.let {
        addDeepLink(it)
    }
}

@JvmSynthetic
internal fun NavController.createNode(
    nodeId: Int, block: ((Bundle) -> Fragment)? = null
) = FragivityFragmentDestination(
    navigatorProvider[FragivityFragmentNavigator::class]
).apply {
    id = nodeId
    this.factory = block
}
