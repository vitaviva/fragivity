package androidx.fragment.app

import java.util.concurrent.ConcurrentHashMap

@PublishedApi
internal object FragmentProviderMap : MutableMap<String, (() -> Fragment)?> by ConcurrentHashMap()