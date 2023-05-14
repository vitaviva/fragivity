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
    return backQueue.removeLast()
}

@JvmSynthetic
internal fun NavController.clearBackStackEntry() {
    backQueue.clear()
}

// ≈ backQueue.size <= 1
@JvmSynthetic
internal fun NavController.isNullRootNode(): Boolean {
    if (backQueue.isEmpty()) return true
    return backQueue.iterator().run {
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
    graph.nodes.valueIterator().forEach { node ->
        node.parent = null
        result.add(node)
    }
    return result
}