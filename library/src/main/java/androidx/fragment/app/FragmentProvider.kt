package androidx.fragment.app

import java.util.concurrent.ConcurrentHashMap

/**
 * @author wangpeng.rocky@bytedance.com
 */
object FragmentProvider : MutableMap<String, (() -> Fragment)?> by ConcurrentHashMap()