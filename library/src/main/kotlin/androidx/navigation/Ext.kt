package androidx.navigation

import android.os.Bundle
import androidx.collection.valueIterator

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
internal fun NavController.removeLastBackStackEntry(): NavBackStackEntry? {
    return mBackStack.removeLast()
}

@JvmSynthetic
internal fun NavController.clearBackStackEntry() {
    mBackStack.clear()
}

// ≈ mBackStack.size <= 1
@JvmSynthetic
internal fun NavController.isNullRootNode(): Boolean {
    if (mBackStack.isEmpty()) return true
    return mBackStack.iterator().run {
        next() // pass NavGraph
        hasNext().not()
    }
}

@JvmSynthetic
internal fun NavController.popBackStack(request: NavDeepLinkRequest, inclusive: Boolean): Boolean {
    val destination = findDestination(request) ?: return false
    return popBackStack(destination.id, inclusive)
}

@JvmSynthetic
internal fun NavController.graphNodes(): List<NavDestination> {
    val result = mutableListOf<NavDestination>()
    graph.mNodes.valueIterator().forEach { node ->
        node.parent = null
        result.add(node)
    }
    return result
}