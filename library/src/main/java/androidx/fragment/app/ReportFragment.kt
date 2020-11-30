package androidx.fragment.app

import android.content.Context
import android.content.res.TypedArray
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.fragivity.R
import com.github.fragivity.swipeback.SwipeBackLayout

internal class ReportFragment : Fragment() {

    internal lateinit var className: String

    private val _real: Class<out Fragment> by lazy {
        Class.forName(className) as Class<out Fragment>
    }

    private val _realFragment by lazy {
        FragmentProvider[className]?.invoke().also { FragmentProvider.remove(className) }
            ?: _real.newInstance()
    }

    internal lateinit var _swipeBackLayout: SwipeBackLayout

    init {
        mChildFragmentManager = ReportFragmentManager()
    }

    var isShow: Boolean
        set(value) {
            (mChildFragmentManager as ReportFragmentManager).isShow = value
        }
        get() {
            return (mChildFragmentManager as ReportFragmentManager).isShow
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mChildFragmentManager.beginTransaction().apply {
            _realFragment.arguments = arguments
            add(R.id.container, _realFragment)
            commitNow()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _swipeBackLayout =
            SwipeBackLayout(requireContext()).apply {
                attachToFragment(
                    this@ReportFragment,
                    inflater.inflate(R.layout.report_layout, container, false)
                        .apply { appendBackground() } // add a default background color to make it opaque

                )
                setEnableGesture(false) //default false
            }
        sharedElementEnterTransition = _realFragment.sharedElementEnterTransition
        sharedElementReturnTransition = _realFragment.sharedElementReturnTransition
        return _swipeBackLayout
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _swipeBackLayout.internalCallOnDestroyView()
    }

    private fun View.appendBackground() {
        val a: TypedArray =
            requireActivity().theme.obtainStyledAttributes(intArrayOf(android.R.attr.windowBackground))
        val background = a.getResourceId(0, 0)
        a.recycle()
        setBackgroundResource(background)
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        mChildFragmentManager.beginTransaction().apply {
//            _realFragment.arguments = savedInstanceState
//            add(R.id.container, _realFragment)
//            commitNow()
//        }
//        super.onActivityCreated(savedInstanceState)
//    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        if (navState != null) {
//            outState.putBundle(NavHostFragment.KEY_NAV_CONTROLLER_STATE, navState)
//        }
//        if (mDefaultNavHost) {
//            outState.putBoolean(NavHostFragment.KEY_DEFAULT_NAV_HOST, true)
//        }
//    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        _realFragment.onCreate(savedInstanceState)
//    }
//
//    override fun onResume() {
//        super.onResume()
//        _realFragment.onResume()
//    }
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        _realFragment.onAttach(context)
//        _realFragment.mHost = this.mHost
//        _realFragment.mChildFragmentManager = this.mChildFragmentManager;
//    }

//    override fun onDetach() {
//        super.onDetach()
//        _realFragment.onDetach()
//    }
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        _realFragment.onActivityCreated(savedInstanceState)
//    }

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        if (!this::_view.isInitialized) {
//            _view = _realFragment.onCreateView(inflater, container, savedInstanceState)
//                ?: throw RuntimeException()
//        }
//        return _view
//    }
//
//    override fun onStart() {
//        super.onStart()
//        _realFragment.onStart()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        _realFragment.onPause()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        _realFragment.onStop()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        _realFragment.onDestroy()
//    }


//    override fun performAttach() {
//        super.performAttach()
//        _realFragment.performAttach()
//        _realFragment.mHost = this.mHost
//        _realFragment.mChildFragmentManager = this.mChildFragmentManager;
//    }
//
//    override fun performActivityCreated(savedInstanceState: Bundle?) {
//        super.performActivityCreated(savedInstanceState)
//        _realFragment.performActivityCreated(savedInstanceState)
//    }
//
//    override fun performCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ) {
//        super.performCreateView(inflater, container, savedInstanceState)
//        _realFragment.performCreateView(inflater, container, savedInstanceState)
//    }

}