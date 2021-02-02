package androidx.fragment.app

import java.util.concurrent.ConcurrentHashMap

@PublishedApi
internal object FragmentProvider : MutableMap<String, (() -> Fragment)?> by ConcurrentHashMap()