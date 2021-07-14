package com.github.fragivity

import android.os.Bundle
import androidx.fragment.app.FragivityFragmentDestination
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragivityFragmentNavigator
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.get
import com.github.fragivity.deeplink.getRouteUri
import kotlin.reflect.KClass

@JvmSynthetic
internal fun NavController.createNavDestination(
    destinationId: Int,
    clazz: KClass<out Fragment>
): FragmentNavigator.Destination {
    return FragmentNavigator.Destination(
        navigatorProvider[FragivityFragmentNavigator::class]
    ).apply {
        id = destinationId
        setClassName(clazz.java.name)
        label = clazz.qualifiedName
        getRouteUri(clazz)?.let {
            addDeepLink(it)
        }
    }
}

@JvmSynthetic
internal fun NavController.createNavDestination(
    destinationId: Int,
    factory: ((Bundle) -> Fragment)? = null
): FragivityFragmentDestination {
    return FragivityFragmentDestination(
        navigatorProvider[FragivityFragmentNavigator::class]
    ).apply {
        id = destinationId
        this.factory = factory
    }
}
