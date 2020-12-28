package androidx.navigation

internal fun NavDestination.removeFromParent() {
    this.parent = null
}