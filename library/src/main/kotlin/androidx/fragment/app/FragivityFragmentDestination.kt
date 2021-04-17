package androidx.fragment.app

import android.os.Bundle
import androidx.navigation.fragment.FragmentNavigator

internal class FragivityFragmentDestination(
    navigator: FragivityFragmentNavigator,
    destinationId: Int,
    var factory: ((Bundle) -> Fragment)? = null
) : FragmentNavigator.Destination(navigator) {

    init {
        id = destinationId
    }

    fun createFragment(args: Bundle?): Fragment {
        val realArgs = args ?: Bundle()

        val fragment = factory!!.invoke(realArgs)
        if (fragment.arguments != null) {
            realArgs.putAll(fragment.arguments)
        }
        fragment.arguments = realArgs
        return fragment
    }
}