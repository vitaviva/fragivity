package com.github.fragivity.example.flow.ui.shop

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.github.fragivity.applyVerticalInOut
import com.github.fragivity.example.flow.R
import com.github.fragivity.example.flow.databinding.FlowFragmentContentBinding
import com.github.fragivity.example.ui.CycleFragment
import com.github.fragivity.example.viewbinding.viewBinding
import com.github.fragivity.navigator
import com.github.fragivity.push

class ContentFragment : Fragment(R.layout.flow_fragment_content) {

    private val binding by viewBinding(FlowFragmentContentBinding::bind)

    private val mTvContent get() = binding.tvContent
    private val mBtnNext get() = binding.btnNext

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBtnNext.setOnClickListener {
            navigator.push(CycleFragment::class) {
                applyVerticalInOut()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        mTvContent.text = "Content:\n${arguments?.getString(ARGS_MENU)}"
    }

    companion object {
        const val ARGS_MENU = "content_args_menu"
        fun newInstance(menu: String) = ContentFragment().apply {
            arguments = bundleOf(ARGS_MENU to menu)
        }
    }
}