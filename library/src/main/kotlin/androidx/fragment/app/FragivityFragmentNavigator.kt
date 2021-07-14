package androidx.fragment.app

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction.OP_ADD
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import com.github.fragivity.plusAssign

@Navigator.Name("ignore")
class FragivityFragmentNavigator(
    private val context: Context,
    private val fragmentManager: FragmentManager,
    private val containerId: Int
) : Navigator<FragmentNavigator.Destination>() {

    private val savedIds = mutableSetOf<String>()

//    private var mIsPendingAddToBackStackOperation = false
//    private var mIsPendingPopBackStackOperation = false

    init {
//        // Need to cooperate with ReportFragmentManager
//        if (fragmentManager is ReportFragmentManager) {
//            fragmentManager.addOnBackStackChangedListener {
//                if (mIsPendingAddToBackStackOperation) {
//                    mIsPendingAddToBackStackOperation = !isBackStackEqual()
//                    val size = fragmentManager.fragments.size
//                    if (size > 1) {
//                        // 切到后台时的生命周期
//                        val fragment = fragmentManager.fragments[size - 2]
//                        // fragment onResume -> onStop
//                        moveFragmentState(fragmentManager, fragment, Fragment.ACTIVITY_CREATED)
//                        setFragmentState(fragmentManager, fragment, Fragment.STARTED)
//                        fragment.mMaxState = Lifecycle.State.STARTED
//                    }
//                } else if (mIsPendingPopBackStackOperation) {
//                    mIsPendingPopBackStackOperation = !isBackStackEqual()
//                    // 回到前台时的生命周期
//                    val fragment = fragmentManager.primaryNavigationFragment
//                        ?: return@addOnBackStackChangedListener
//                    // fragment (true) ?: onStart : onCreateView -> onResume
//                    if (fragment.mState == Fragment.STARTED) {
//                        fragment.mMaxState = Lifecycle.State.RESUMED
//                        fragment.mState = Fragment.ACTIVITY_CREATED
//                        moveFragmentState(fragmentManager, fragment, Fragment.RESUMED)
//                    }
//                    setMaxLifecycle(fragment, Lifecycle.State.RESUMED)
//                }
//            }
//        }
    }

    override fun createDestination(): FragmentNavigator.Destination {
        return FragmentNavigator.Destination(this)
    }

    private fun createFragment(
        destination: FragmentNavigator.Destination,
        args: Bundle?
    ): Fragment {
        if (destination is FragivityFragmentDestination) {
            return destination.createFragment(args)
        }

        var className = destination.className
        if (className[0] == '.') {
            className = context.packageName + className
        }

        val fragment = fragmentManager.fragmentFactory.instantiate(context.classLoader, className)
        fragment.arguments = args
        return fragment
    }

    override fun navigate(
        entries: List<NavBackStackEntry>,
        navOptions: NavOptions?,
        navigatorExtras: Extras?
    ) {
        if (fragmentManager.isStateSaved) {
            Log.i(
                TAG, "Ignoring navigate() call: FragmentManager has already saved its state"
            )
            return
        }
        for (entry in entries) {
            navigate(entry, navOptions, navigatorExtras)
        }
    }

    private fun navigate(
        entry: NavBackStackEntry,
        navOptions: NavOptions?,
        navigatorExtras: Extras?
    ) {

        val backStack = state.backStack.value
        val initialNavigation = backStack.isEmpty()

        val restoreState = (
            navOptions != null && !initialNavigation &&
                navOptions.shouldRestoreState() &&
                savedIds.remove(entry.id)
            )
        if (restoreState) {
            fragmentManager.restoreBackStack(entry.id)
            state.push(entry)
            return
        }

        val destination = entry.destination as FragmentNavigator.Destination
        val args = entry.arguments

        val ft = fragmentManager.beginTransaction()

        var enterAnim = navOptions?.enterAnim ?: -1
        var exitAnim = navOptions?.exitAnim ?: -1
        var popEnterAnim = navOptions?.popEnterAnim ?: -1
        var popExitAnim = navOptions?.popExitAnim ?: -1
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = if (enterAnim != -1) enterAnim else 0
            exitAnim = if (exitAnim != -1) exitAnim else 0
            popEnterAnim = if (popEnterAnim != -1) popEnterAnim else 0
            popExitAnim = if (popExitAnim != -1) popExitAnim else 0
            ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
        }

        val destId = destination.id

        val fragment = createFragment(destination, args)
//        ft.add(containerId, fragment, generateBackStackName(backStack.size, destId))
        ft.replace(containerId, fragment)

//        val prevFragment = fragmentManager.primaryNavigationFragment
        ft.setPrimaryNavigationFragment(fragment)

        val isSingleTopReplacement = (
            navOptions != null && !initialNavigation &&
                navOptions.shouldLaunchSingleTop() &&
                backStack.last().destination.id == destId
            )

        // when popsSelf == true close preFrag as SingleTop
        // see https://github.com/vitaviva/fragivity/issues/26
        val isPopSelf = args?.getBoolean(KEY_POP_SELF, false) == true

        val isAdded = when {
            initialNavigation -> {
                true
            }
            isSingleTopReplacement || isPopSelf -> {
                if (backStack.size > 1) {
                    fragmentManager.popBackStack(
                        entry.id,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                    ft.addToBackStack(entry.id)
                }
//                if (prevFragment != null) {
//                    ft.remove(prevFragment)
//                    fragment.mTag = generateBackStackName(backStack.size - 1, destId)
//                    val size = fragmentManager.mBackStack?.size ?: 0
//                    if (size > 0) {
//                        fragmentManager.mBackStack[size - 1].mOps
//                            .filter { it.mCmd == OP_ADD && it.mFragment == prevFragment }
//                            .forEach { it.mFragment = fragment }
//                    }
//                }
                false
            }
            else -> {
                ft.addToBackStack(entry.id)
//                ft.addToBackStack(generateBackStackName(backStack.size + 1, destId))
//                mIsPendingAddToBackStackOperation = true
//                if (prevFragment != null) {
//                    ft.hide(prevFragment)
//                }
                true
            }
        }

        if (navigatorExtras is FragmentNavigator.Extras) {
            for ((key, value) in navigatorExtras.sharedElements) {
                ft.addSharedElement(key, value)
            }
        }

        ft.setReorderingAllowed(true)
        ft.commit()

        if (isAdded) {
            state.push(entry)
        }
    }

//    private fun generateBackStackName(backStackIndex: Int, destinationId: Int): String {
//        return "${backStackIndex}#${destinationId}"
//    }
//
//    private fun getDestinationId(backStackName: String): Int {
//        val split = backStackName.split("#")
//        if (split.size != 2) {
//            throw IllegalStateException(
//                "Invalid back stack entry on the "
//                    + "NavHostFragment's back stack - use getChildFragmentManager() "
//                    + "if you need to do custom FragmentTransactions from within "
//                    + "Fragments created via your navigation graph."
//            )
//        }
//        return split[1].toIntOrNull()
//            ?: throw java.lang.IllegalStateException(
//                "Invalid back stack entry on the "
//                    + "NavHostFragment's back stack - use getChildFragmentManager() "
//                    + "if you need to do custom FragmentTransactions from within "
//                    + "Fragments created via your navigation graph."
//            )
//    }
//
//    private fun isBackStackEqual(): Boolean {
//        val backStack = state.backStack.value
//        val fragmentBackStackCount = fragmentManager.backStackEntryCount
//        if (backStack.size != fragmentBackStackCount + 1) {
//            return false
//        }
//
//        var backStackIndex = fragmentBackStackCount - 1
//        val backStackIterator = backStack.asReversed().iterator()
//        while (backStackIterator.hasNext() && backStackIndex >= 0) {
//            val backStackEntry = backStackIterator.next()
//
//            val fragmentDestId = getDestinationId(
//                fragmentManager.getBackStackEntryAt(backStackIndex--).name!!
//            )
//
//            if (backStackEntry.destination.id != fragmentDestId) {
//                return false
//            }
//        }
//        return true
//    }

    override fun popBackStack(popUpTo: NavBackStackEntry, savedState: Boolean) {
        if (fragmentManager.isStateSaved) {
            Log.i(TAG, "Ignoring popBackStack() call: FragmentManager has already saved its state")
            return
        }

        if (savedState) {
            val beforePopList = state.backStack.value
            val initialEntry = beforePopList.first()
            // Get the set of entries that are going to be popped
            val poppedList = beforePopList.subList(
                beforePopList.indexOf(popUpTo),
                beforePopList.size
            )
            // Now go through the list in reversed order (i.e., started from the most added)
            // and save the back stack state of each.
            for (entry in poppedList.reversed()) {
                if (entry == initialEntry) {
                    Log.i(
                        TAG,
                        "FragmentManager cannot save the state of the initial destination $entry"
                    )
                } else {
                    fragmentManager.saveBackStack(entry.id)
                    savedIds += entry.id
                }
            }
        } else {
            fragmentManager.popBackStack(
                popUpTo.id,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
//            val backStack = state.backStack.value
//            fragmentManager.popBackStack(
//                generateBackStackName(backStack.size, popUpTo.destination.id),
//                FragmentManager.POP_BACK_STACK_INCLUSIVE
//            )
//            mIsPendingPopBackStackOperation = true
        }
        state.pop(popUpTo, savedState)
    }

    override fun onSaveState(): Bundle? {
        if (savedIds.isEmpty()) {
            return null
        }
        return bundleOf(KEY_SAVED_IDS to ArrayList(savedIds))
    }

    override fun onRestoreState(savedState: Bundle) {
        val savedIds = savedState.getStringArrayList(KEY_SAVED_IDS)
        if (savedIds != null) {
            this.savedIds.clear()
            this.savedIds += savedIds
        }
    }

    fun restoreTopFragment(destinationId: Int, newBundle: Bundle?) {
//        val topFragment = findTopFragment(destinationId) ?: return
//        // update args
//        topFragment += newBundle
//        // run onResume
//        setMaxLifecycle(topFragment, Lifecycle.State.STARTED)
//        setMaxLifecycle(topFragment, Lifecycle.State.RESUMED)
    }

//    private fun findTopFragment(destinationId: Int): Fragment? {
//        if (savedIds.isEmpty()) return null
//
//        val backStack = state.backStack.value
//
//        var index = savedIds.size - 1
//        backStack.asReversed().forEach { backStackEntry ->
//            if (destinationId == backStackEntry.destination.id) {
//                return fragmentManager.findFragment(index, destinationId)
//            }
//            index--
//        }
//        return null
//    }
//
//    private fun FragmentManager.findFragment(backStackIndex: Int, destinationId: Int): Fragment? {
//        return findFragmentByTag(generateBackStackName(backStackIndex, destinationId))
//    }
//
//    private fun setMaxLifecycle(fragment: Fragment, state: Lifecycle.State) {
//        fragmentManager.commit { setMaxLifecycle(fragment, state) }
//    }

    companion object {
        private const val TAG = "FragivityNavigator"
        private const val KEY_SAVED_IDS = "Fragivity:navigator:savedIds"
        internal const val KEY_POP_SELF = "Fragivity:PopSelf"
    }
}
