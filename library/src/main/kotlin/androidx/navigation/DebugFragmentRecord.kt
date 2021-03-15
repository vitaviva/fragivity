package androidx.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.ReportFragment

internal data class DebugFragmentRecord(
    val fragmentName: CharSequence,
    val childrenList: List<DebugFragmentRecord>
)

internal fun FragmentActivity.getFragmentRecords(): List<DebugFragmentRecord> {
    val fragmentList = supportFragmentManager.fragments
    if (fragmentList.isNullOrEmpty()) return emptyList()

    val fragmentRecordList = mutableListOf<DebugFragmentRecord>()
    fragmentList.forEach { addDebugFragmentRecord(fragmentRecordList, it) }
    return fragmentRecordList
}

private fun addDebugFragmentRecord(
    fragmentRecords: MutableList<DebugFragmentRecord>,
    fragment: Fragment?
) {
    if (fragment == null) return

    // Ignore ReportFragment
    if (fragment is ReportFragment) {
        fragmentRecords.addAll(getChildFragmentRecords(fragment))
        return
    }

    val name = fragment::class.java.simpleName
    fragmentRecords.add(DebugFragmentRecord(name, getChildFragmentRecords(fragment)))
}

private fun getChildFragmentRecords(parentFragment: Fragment): List<DebugFragmentRecord> {
    // java.lang.IllegalStateException: Fragment .. has not been attached yet.
    if (!parentFragment.isAdded) return emptyList()

    val fragmentList = parentFragment.childFragmentManager.fragments
    if (fragmentList.isNullOrEmpty()) return emptyList()

    val fragmentRecordList = mutableListOf<DebugFragmentRecord>()
    fragmentList.forEach { addDebugFragmentRecord(fragmentRecordList, it) }
    return fragmentRecordList
}