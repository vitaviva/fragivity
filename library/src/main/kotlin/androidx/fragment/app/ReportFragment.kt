package androidx.fragment.app

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.Context
import android.content.res.TypedArray
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import com.github.fragivity.R
import com.github.fragivity.swipeback.SwipeBackLayout
import java.lang.reflect.Constructor

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
                val frag = FragmentProviderMap[className]?.invoke()
                    .also { FragmentProviderMap.remove(className) }
                    ?: run {
                        val constructor = findMatchingConstructor(_real)
                            ?: error("${_real.simpleName} need a empty constructor, otherwise it cannot be rebuilt after process killed")
                        constructor.newInstance()
                    }
                frag.apply {
                    retainInstance = FRAGMENT_RETAIN_INSTANCE
                }
            }.also { _vm.fragment = it }
        }


    internal lateinit var _swipeBackLayout: SwipeBackLayout

    //fix https://github.com/vitaviva/fragivity/issues/7
    @JvmField
    internal var disableAnimOnce = false

    @PublishedApi
    internal var isInForeground: Boolean
        set(value) {
            _vm.isShowing = value
        }
        get() {
            return _vm.isShowing
        }

    init {
        mChildFragmentManager = ReportFragmentManager()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (!_vm.isRecreating) {
            addRealFragment()
        } else {
            _vm.isRecreating = false
        }
    }

    private fun addRealFragment() {
        check(mChildFragmentManager.fragments.isEmpty()) {
            "ReportFragmentManager can't hold fragment more than one "
        }
        mChildFragmentManager.beginTransaction().apply {
            _realFragment.arguments = requireNotNull(arguments).apply {
                if (_realFragment.arguments != null) {
                    putAll(_realFragment.arguments)
                }
            }
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

    override fun onDestroy() {
        super.onDestroy()
        /**
         * https://github.com/vitaviva/fragivity/issues/4
         * this flag used to avoid [_realFragment] to perform onCreate as configuration changed
         */
        _vm.isRecreating = requireActivity().isChangingConfigurations

    }


    private fun View.appendBackground() {
        val a: TypedArray =
            requireActivity().theme.obtainStyledAttributes(intArrayOf(android.R.attr.windowBackground))
        val background = a.getResourceId(0, 0)
        a.recycle()
        setBackgroundResource(background)
    }

    override fun onCreateAnimator(transit: Int, enter: Boolean, nextAnim: Int): Animator? {
        if (nextAnim == 0) return super.onCreateAnimator(transit, enter, nextAnim)

        if (disableAnimOnce) {
            disableAnimOnce = false
            val animator = AnimatorInflater.loadAnimator(context, R.animator.no_anim)
            if (animator != null) {
                return animator
            }
        }

        return super.onCreateAnimator(transit, enter, nextAnim)

    }

    data class FragmentViewModel(
        var fragment: Fragment? = null,
        var isRecreating: Boolean = false,
        var isShowing: Boolean = true
    ) : ViewModel()

    internal companion object {
        const val REAL_FRAGMENT = "real"
        private const val FRAGMENT_RETAIN_INSTANCE = true
    }

}


private fun <T> findMatchingConstructor(
    modelClass: Class<T>
): Constructor<T>? {
    for (constructor in modelClass.constructors) {
        val parameterTypes = constructor.parameterTypes
        if (parameterTypes.isEmpty()) {
            return constructor as Constructor<T>
        }
    }
    return null
}