package com.github.fragivity.example.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.github.fragivity.applyArguments
import com.github.fragivity.applyVerticalInOut
import com.github.fragivity.example.base.R
import com.github.fragivity.example.base.databinding.FragmentCycleBinding
import com.github.fragivity.example.base.initToolbarNav
import com.github.fragivity.example.viewbinding.viewBinding
import com.github.fragivity.navigator
import com.github.fragivity.push

class CycleFragment : Fragment(R.layout.fragment_cycle) {

    private val binding by viewBinding(FragmentCycleBinding::bind)

    private val mToolbar get() = binding.content.toolbar
    private val mTvName get() = binding.tvName
    private val mBtnNext get() = binding.btnNext
    private val mBtnNextWithFinish get() = binding.btnNextWithFinish

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