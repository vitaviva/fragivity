package com.github.fragivity.example.flow.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.github.fragivity.applyVerticalInOut
import com.github.fragivity.example.base.hideSoftInput
import com.github.fragivity.example.base.initToolbarNav
import com.github.fragivity.example.base.showToast
import com.github.fragivity.example.flow.R
import com.github.fragivity.example.ui.CycleFragment
import com.github.fragivity.navigator
import com.github.fragivity.push
import kotlinx.android.synthetic.main.flow_content_toolbar.*
import kotlinx.android.synthetic.main.flow_fragment_modify_detail.*

class ModifyDetailFragment : Fragment(R.layout.flow_fragment_modify_detail) {

    private val mToolbar get() = toolbar
    private val mEtModifyTitle get() = et_modify_title
    private val mBtnModify get() = btn_modify
    private val mBtnNext get() = btn_next

    private var mTitle = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mTitle = arguments?.getString(ARGS_TITLE) ?: ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mToolbar.setTitle(R.string.start_result_test)
        initToolbarNav(mToolbar)

        mEtModifyTitle.setText(mTitle)

        mBtnModify.setOnClickListener {
            requireActivity().supportFragmentManager
                .setFragmentResult(
                    DetailFragment.KEY_RESULT_TITLE,
                    bundleOf(DetailFragment.ARGS_TITLE to mEtModifyTitle.text.toString())
                )
            showToast(R.string.modify_success)
        }
        mBtnNext.setOnClickListener {
            navigator.push(CycleFragment::class) {
                applyVerticalInOut()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        hideSoftInput()
    }

    companion object {
        const val ARGS_TITLE = "modify_detail_args_title"
    }

}