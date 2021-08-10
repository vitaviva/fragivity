package com.github.fragivity

import androidx.core.net.toUri
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDestination

const val DEFAULT_ROOT_ROUTE = "root"

@JvmSynthetic
internal fun createRoute(route: String) = "android-app://androidx.navigation.fragivity/$route"

@JvmSynthetic
internal fun NavDestination.appendRootRoute() {
    if (!hasRootRoute()) {
        appendDeepRoute(DEFAULT_ROOT_ROUTE)
    }
}

@JvmSynthetic
internal fun NavDestination.appendDeepRoute(route: String) {
    addDeepLink(createRoute(route))
}

@JvmSynthetic
internal fun NavDestination.hasDeepLink(route: String): Boolean {
    return hasDeepLink(route.toRequest())
}

@JvmSynthetic
internal fun NavDestination.hasRootRoute(): Boolean {
    return hasDeepLink(DEFAULT_ROOT_ROUTE)
}

@JvmSynthetic
internal fun String.toRequest(): NavDeepLinkRequest {
    return NavDeepLinkRequest.Builder.fromUri(createRoute(this).toUri()).build()
}