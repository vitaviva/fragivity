package androidx.fragment.app;

class ReportFragmentManager extends FragmentManager {

    boolean isShow = true;

    @Override
    void dispatchResume() {
        if (isShow) super.dispatchResume();
    }

    @Override
    void dispatchStart() {
        if (isShow) super.dispatchStart();
    }

    @Override
    void dispatchDestroyView() {
        if (isShow) super.dispatchDestroyView();
    }

    @Override
    void dispatchCreate() {
        if (isShow) super.dispatchCreate();
    }

    @Override
    void dispatchPause() {
        if (isShow) super.dispatchPause();
    }

    @Override
    void dispatchActivityCreated() {
        if (isShow) super.dispatchPause();
    }
}
