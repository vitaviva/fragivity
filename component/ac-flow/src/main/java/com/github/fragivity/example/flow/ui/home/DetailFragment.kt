package com.github.fragivity.example.flow.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import com.github.fragivity.applyArguments
import com.github.fragivity.applyVerticalInOut
import com.github.fragivity.example.base.doOnIdle
import com.github.fragivity.example.base.initToolbarNav
import com.github.fragivity.example.flow.R
import com.github.fragivity.example.flow.databinding.FlowFragmentDetailBinding
import com.github.fragivity.example.viewbinding.viewBinding
import com.github.fragivity.navigator
import com.github.fragivity.push

class DetailFragment : Fragment(R.layout.flow_fragment_detail) {

    private val binding by viewBinding(FlowFragmentDetailBinding::bind)

    private val mToolbar get() = binding.content.toolbar
    private val mFab get() = binding.fab
    private val mTvContent get() = binding.contentDetail.tvContent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().supportFragmentManager
            .setFragmentResultListener(KEY_RESULT_TITLE, this, FragmentResultListener { _, result ->
                mToolbar.title = result.getString(ARGS_TITLE)
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mToolbar.title = requireArguments().getString(ARGS_TITLE)
        initToolbarNav(mToolbar)

        mFab.setOnClickListener {
            navigator.push(ModifyDetailFragment::class) {
                applyVerticalInOut()
                applyArguments(ModifyDetailFragment.ARGS_TITLE to mToolbar.title.toString())
            }
        }

        doOnIdle {
            mTvContent.setText(R.string.large_text)
        }
    }

    companion object {
        const val ARGS_TITLE = "detail_args_title"
        const val KEY_RESULT_TITLE = "detail_key_result_title"
        fun newInstance(title: String) = DetailFragment().apply {
            arguments = bundleOf(ARGS_TITLE to title)
        }
    }
}