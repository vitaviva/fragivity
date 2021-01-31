package androidx.fragment.app

internal class ReportFragmentManager : FragmentManager() {

    init {
        addOnBackStackChangedListener {
            check(mBackStack.size > 1) { "ReportFragmentManager can't hold fragment more than one " }
        }
    }

    private val reportFragment by lazy {
        parent as ReportFragment
    }

    private val isShow
        get() = reportFragment.isShow

    override fun dispatchResume() {
        if (isShow) super.dispatchResume()
    }

    override fun dispatchStart() {
        if (isShow) super.dispatchStart()
    }

    override fun dispatchCreate() {
        if (isShow) super.dispatchCreate()
    }

    override fun dispatchActivityCreated() {
        if (isShow) super.dispatchActivityCreated()
    }

    override fun dispatchPause() {
        if (isShow) super.dispatchPause()
    }

    override fun dispatchStop() {
        if (isShow) super.dispatchStop()
    }

    override fun dispatchDestroyView() {
        if (isShow) super.dispatchDestroyView()
    }

}