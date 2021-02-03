package androidx.fragment.app

internal class ReportFragmentManager : FragmentManager() {

    private val reportFragment by lazy {
        parent as ReportFragment
    }

    private val isInForeground
        get() = reportFragment.isInForeground

    override fun dispatchResume() {
        if (isInForeground) super.dispatchResume()
    }

    override fun dispatchStart() {
        if (isInForeground) super.dispatchStart()
    }

    override fun dispatchCreate() {
        if (isInForeground) super.dispatchCreate()
    }

    override fun dispatchActivityCreated() {
        if (isInForeground) super.dispatchActivityCreated()
    }

    override fun dispatchPause() {
        if (isInForeground) super.dispatchPause()
    }

    override fun dispatchStop() {
        if (isInForeground) super.dispatchStop()
    }

    override fun dispatchDestroyView() {
        if (isInForeground) super.dispatchDestroyView()
    }

}