package androidx.navigation

import android.os.Bundle

@JvmSynthetic
internal fun NavController.findDestination(request: NavDeepLinkRequest): NavDestination? {
    return graph.matchDeepLink(request)?.destination
}

@JvmSynthetic
internal fun NavController.findDestinationAndArgs(
    request: NavDeepLinkRequest
): Pair<NavDestination, Bundle?>? {
    return graph.matchDeepLink(request)?.run { destination to matchingArgs }
}

@JvmSynthetic
internal fun NavController.removeLastBackStackEntry() {
    if (mBackStack.isNotEmpty()) {
        mBackStack.removeLast()
    }
}

@JvmSynthetic
internal fun NavController.popBackStack(request: NavDeepLinkRequest, inclusive: Boolean): Boolean {
    val destination = findDestination(request) ?: return false
    return popBackStack(destination.id, inclusive)
}
