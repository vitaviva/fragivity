package com.github.fragivity.example.flow.ui.swipeback

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.github.fragivity.applyHorizontalInOut
import com.github.fragivity.example.base.initToolbarNav
import com.github.fragivity.example.flow.R
import com.github.fragivity.example.flow.ui.MainActivity
import com.github.fragivity.navigator
import com.github.fragivity.push
import com.github.fragivity.swipeback.swipeBackLayout
import kotlinx.android.synthetic.main.flow_content_toolbar.*
import kotlinx.android.synthetic.main.flow_fragment_swipe_back_first.*

class FirstSwipeBackFragment : Fragment(R.layout.flow_fragment_swipe_back_first) {

    private val mToolbar get() = toolbar
    private val mNext get() = btn

    private var isFirst = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isFirst = arguments?.getBoolean(ARGS_IS_FIRST) ?: false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mToolbar.title = "SwipeBackActivity's Fragment"
        initToolbarNav(mToolbar)

        mNext.setOnClickListener {
            navigator.push(RecyclerSwipeBackFragment::class) {
                applyHorizontalInOut()
            }
        }

        swipeBackLayout.setEnableGesture(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFirst) {
            requireActivity().supportFragmentManager
                .setFragmentResult(MainActivity.KEY_RESULT_OPEN_DRAW, Bundle.EMPTY)
        }
    }

    companion object {
        const val ARGS_IS_FIRST = "first_swipe_back_is_first"
    }
}