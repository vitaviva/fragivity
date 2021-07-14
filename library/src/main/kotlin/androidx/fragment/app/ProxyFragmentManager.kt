@file:JvmName("ReportHelper")

package androidx.fragment.app

import android.os.Parcelable
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.NavHostFragment

internal class ReportFragmentManager : FragmentManager() {

    override fun dispatchStart() {
        forEachBackFragment { fragment ->
            if (fragment.mState == Fragment.ACTIVITY_CREATED) {
                setFragmentState(this, fragment, Fragment.STARTED)
            }
        }
        super.dispatchStart()
    }

    override fun dispatchStop() {
        forEachBackFragment { fragment ->
            if (fragment.mState == Fragment.STARTED) {
                setFragmentState(this, fragment, Fragment.ACTIVITY_CREATED)
            }
        }
        super.dispatchStop()
    }

    override fun saveAllStateInternal(): Parcelable {
        if (parent?.activity?.isChangingConfigurations == true) {
            forEachBackFragment { fragment ->
                if (fragment.mMaxState != Lifecycle.State.CREATED) {
                    fragment.mMaxState = Lifecycle.State.CREATED
                }
            }
        }
        return super.saveAllStateInternal()
    }

    private fun forEachBackFragment(block: (Fragment) -> Unit) {
        val fragments = fragments
        if (fragments.size > 1) {
            var fragment: Fragment
            for (i in 0..fragments.size - 2) {
                fragment = fragments[i]
                if (fragment is NavHostFragment) continue
                block(fragments[i])
            }
        }
    }
}

private class FragmentFactoryProxy(
    private val factory: FragmentFactory
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        val fragment = factory.instantiate(classLoader, className)
        if (fragment is NavHostFragment) {
            fragment.setupReportFragmentManager()
        }
        return fragment
    }
}

private fun Fragment.setupFragmentManager(manager: FragmentManager) {
    mChildFragmentManager = manager
}

fun NavHostFragment.setupReportFragmentManager() {
//    setupFragmentManager(ReportFragmentManager())
}

fun FragmentManager.proxyFragmentFactory() {
    val oldFactory = fragmentFactory
    if (oldFactory !is FragmentFactoryProxy) {
        fragmentFactory = FragmentFactoryProxy(oldFactory)
    }
}

fun FragmentActivity.proxyFragmentFactory() {
    supportFragmentManager.proxyFragmentFactory()
}