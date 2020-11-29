package androidx.fragment.app

internal class ReportFragmentManager : FragmentManager() {

    init {
        addOnBackStackChangedListener {
            if (mBackStack.size > 1) throw IllegalStateException("ReportFragmentManager can't hold fragment more than one ")
        }
    }

    internal var isShow = true
    public override fun dispatchResume() {
        if (isShow) super.dispatchResume()
    }

    public override fun dispatchStart() {
        if (isShow) super.dispatchStart()
    }

    public override fun dispatchDestroyView() {
        if (isShow) super.dispatchDestroyView()
    }

    public override fun dispatchCreate() {
        if (isShow) super.dispatchCreate()
    }

    public override fun dispatchPause() {
        if (isShow) super.dispatchPause()
    }

    public override fun dispatchActivityCreated() {
        if (isShow) super.dispatchPause()
    }
}