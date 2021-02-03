package androidx.navigation

@JvmSynthetic
internal fun NavDestination.removeFromParent() {
    this.parent = null
}