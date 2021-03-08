package androidx.fragment.app

@JvmSynthetic
internal fun FragmentManager.getBackStack() =
    mBackStack ?: emptyList<BackStackRecord>()

