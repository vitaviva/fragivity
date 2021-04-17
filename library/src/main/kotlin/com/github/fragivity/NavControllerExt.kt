package com.github.fragivity

import android.os.Bundle
import androidx.fragment.app.FragivityFragmentDestination
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragivityFragmentNavigator
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorDestinationBuilder
import androidx.navigation.get
import com.github.fragivity.deeplink.getRouteUri
import kotlin.reflect.KClass

@JvmSynthetic
internal fun NavController.createNavDestination(
    destinationId: Int,
    clazz: KClass<out Fragment>
): FragmentNavigator.Destination {
    return (FragmentNavigatorDestinationBuilder(
        navigatorProvider[FragivityFragmentNavigator::class],
        destinationId,
        clazz
    ).apply {
        label = clazz.qualifiedName
        getRouteUri(clazz)?.let {
            deepLink {
                uriPattern = it
            }
        }
    }).build()
}

@JvmSynthetic
internal fun NavController.createNavDestination(
    destinationId: Int,
    factory: ((Bundle) -> Fragment)? = null
): FragivityFragmentDestination {
    return FragivityFragmentDestination(
        navigatorProvider[FragivityFragmentNavigator::class],
        destinationId,
        factory
    )
}
