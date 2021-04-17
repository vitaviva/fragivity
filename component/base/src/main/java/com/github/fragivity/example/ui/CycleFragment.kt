package com.github.fragivity.example.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.github.fragivity.applyArguments
import com.github.fragivity.applyVerticalInOut
import com.github.fragivity.example.base.R
import com.github.fragivity.example.base.initToolbarNav
import com.github.fragivity.navigator
import com.github.fragivity.push
import kotlinx.android.synthetic.main.content_toolbar.*
import kotlinx.android.synthetic.main.fragment_cycle.*

class CycleFragment : Fragment(R.layout.fragment_cycle) {

    private val mToolbar get() = toolbar
    private val mTvName get() = tv_name
    private val mBtnNext get() = btn_next
    private val mBtnNextWithFinish get() = btn_next_with_finish

    private var num = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        num = arguments?.getInt(ARGS_NUM, 1) ?: 1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = "CyclerFragment $num"

        mToolbar.title = title
        initToolbarNav(mToolbar)

        mTvName.text = title

        mBtnNext.setOnClickListener {
            navigator.push(CycleFragment::class) {
                applyVerticalInOut()
                applyArguments(ARGS_NUM to num + 1)
            }
        }
        mBtnNextWithFinish.setOnClickListener {
            navigator.push(CycleFragment::class) {
                popSelf = true
                applyVerticalInOut()
                applyArguments(ARGS_NUM to num + 1)
            }
        }
    }

    companion object {
        const val ARGS_NUM = "cycler_args_num"
    }
}