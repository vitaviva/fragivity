package androidx.fragment.app

import android.content.Context
import android.content.res.TypedArray
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import com.github.fragivity.R
import com.github.fragivity.swipeback.SwipeBackLayout

internal class ReportFragment : Fragment() {

    private val _vm: FragmentViewModel by viewModels()

    private val className by lazy {
        requireNotNull(arguments?.getString(REAL_FRAGMENT))
    }

    private val _real: Class<out Fragment> by lazy {
        Class.forName(className) as Class<out Fragment>
    }


    internal val _realFragment: Fragment
        get() {
            return _vm.fragment ?: run {
                val frag = FragmentProvider[className]?.invoke()
                    .also { FragmentProvider.remove(className) }
                    ?: _real.newInstance()
                frag.apply {
                    retainInstance = FRAGMENT_RETAIN_INSTANCE
                }
            }.also { _vm.fragment = it }
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

    internal companion object {
        const val REAL_FRAGMENT = "real"
    }

//    override fun performDetach() {
//        super.performDetach()
//        mChildFragmentManager = reportFragmentManager.apply { dispatchDestroy() }
//    }


    data class FragmentViewModel(var fragment: Fragment? = null) : ViewModel()
}

const val FRAGMENT_RETAIN_INSTANCE = true