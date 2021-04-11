package androidx.fragment.app

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.res.use
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModel
import com.github.fragivity.navigator
import com.github.fragivity.pop
import com.github.fragivity.requirePreviousFragment
import com.github.fragivity.swipeback.SwipeBackLayout
import java.lang.reflect.Constructor

internal class ReportFragment : Fragment() {

    private val _vm: FragmentViewModel by viewModels()

    private val className by lazy(LazyThreadSafetyMode.NONE) {
        requireNotNull(arguments?.getString(REAL_FRAGMENT))
    }

    @Suppress("UNCHECKED_CAST")
    private val _real: Class<out Fragment> by lazy(LazyThreadSafetyMode.NONE) {
        Class.forName(className) as Class<out Fragment>
    }

    @Suppress("DEPRECATION")
    private val _realFragment: Fragment
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

    internal var _swipeBackLayout: SwipeBackLayout? = null

    //fix https://github.com/vitaviva/fragivity/issues/7
    @JvmField
    internal var disableAnimOnce = false

    @PublishedApi
    internal var isInForeground: Boolean
        set(value) {
            _vm.isShowing = value
        }
        get() = _vm.isShowing

    init {
        mChildFragmentManager = ReportFragmentManager()
    }

    private val layoutId = ViewCompat.generateViewId()

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
            add(layoutId, _realFragment)
            commitNow()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val layout = FrameLayout(requireContext())
        layout.id = layoutId
        layout.appendBackground()

        sharedElementEnterTransition = _realFragment.sharedElementEnterTransition
        sharedElementReturnTransition = _realFragment.sharedElementReturnTransition

        return SwipeBackLayout(requireContext()).apply {
            attachToFragment(this@ReportFragment, layout)
            setEnableGesture(false) // default false
            addSwipeListener(object : SwipeBackLayout.SimpleOnSwipeListener() {
                override fun onDragFinished(isActivity: Boolean) {
                    if (!isActivity) {
                        disableAnimOnce = true
                        (requirePreviousFragment() as? ReportFragment)?.disableAnimOnce = true
                        navigator.pop()
                    }
                }
            })
            _swipeBackLayout = this
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _swipeBackLayout?.internalCallOnDestroyView()
        _swipeBackLayout = null
    }

    override fun onDestroy() {
        super.onDestroy()
        /**
         * https://github.com/vitaviva/fragivity/issues/4
         * this flag used to avoid [_realFragment] to perform onCreate as configuration changed
         */
        _vm.isRecreating = requireActivity().isChangingConfigurations
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
        private const val REAL_FRAGMENT = "real"
        private const val FRAGMENT_RETAIN_INSTANCE = true

        @JvmStatic
        fun newInstance(realFragmentClassName: String, args: Bundle?) = ReportFragment().apply {
            val bundle = Bundle()
            if (args != null) bundle.putAll(args)
            bundle.putString(REAL_FRAGMENT, realFragmentClassName)
            arguments = bundle
        }
    }
}

private fun View.appendBackground() {
    context.theme.obtainStyledAttributes(intArrayOf(android.R.attr.windowBackground)).use {
        val background = it.getResourceId(0, 0)
        setBackgroundResource(background)
    }
}

@Suppress("UNCHECKED_CAST")
private fun <T> findMatchingConstructor(modelClass: Class<T>): Constructor<T>? {
    for (constructor in modelClass.constructors) {
        val parameterTypes = constructor.parameterTypes
        if (parameterTypes.isEmpty()) {
            return constructor as Constructor<T>
        }
    }
    return null
}