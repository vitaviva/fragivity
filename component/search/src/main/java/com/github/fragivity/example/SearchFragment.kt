package com.github.fragivity.example

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.github.fragivity.example.search.R
import com.github.fragivity.navigator
import com.github.fragivity.router.popTo
import com.github.fragivity.router.push
import kotlinx.android.synthetic.main.search_fragment.*

class SearchFragment : Fragment(R.layout.search_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        input.setText(requireArguments().getString("keyword", ""))
        btn_go_to_feed.setOnClickListener {
            navigator.push("feed")
        }
        btn_go_back_feed.setOnClickListener {
            navigator.popTo("feed")
        }
        btn_go_back_root.setOnClickListener {
            navigator.popTo("root")
        }
    }

    companion object {
        fun newInstance(keyword: String? = null) = SearchFragment().apply {
            if (keyword != null) {
                arguments = bundleOf("keyword" to keyword)
            }
        }
    }
}