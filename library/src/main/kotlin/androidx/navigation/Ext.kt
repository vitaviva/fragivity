package androidx.navigation

import android.os.Bundle
import com.github.fragivity.NavOptions
import com.github.fragivity.toBundle
import com.github.fragivity.totExtras
import com.github.fragivity.totOptions

@JvmSynthetic
internal fun NavDestination.removeFromParent() {
    this.parent = null
}

@JvmSynthetic
internal fun NavController.findDestination(request: NavDeepLinkRequest): NavDestination? {
    return graph.matchDeepLink(request)?.destination
}

@JvmSynthetic
internal fun NavController.navigate(request: NavDeepLinkRequest, navOptions: NavOptions) {
    val deepLinkMatch = graph.matchDeepLink(request) ?: return
    val destination = deepLinkMatch.destination
    val uriArgs = destination.addInDefaultArgs(deepLinkMatch.matchingArgs)
    val optionArgs = navOptions.toBundle()
    navigate(destination.id, uriArgs + optionArgs, navOptions.totOptions(), navOptions.totExtras())
}

@JvmSynthetic
internal fun NavController.popBackStack(request: NavDeepLinkRequest, inclusive: Boolean): Boolean {
    val destination = findDestination(request) ?: return false
    return popBackStack(destination.id, inclusive)
}

private operator fun Bundle?.plus(optionArgs: Bundle?): Bundle? {
    if (this == null && optionArgs == null) return null
    if (this == null) return optionArgs
    if (optionArgs == null) return this
    return Bundle().apply {
        putAll(this@plus)
        putAll(optionArgs)
    }
}
