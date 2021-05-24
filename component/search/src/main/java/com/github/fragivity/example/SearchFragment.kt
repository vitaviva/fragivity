package com.github.fragivity.example

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.github.fragivity.example.search.R
import com.github.fragivity.example.search.databinding.SearchFragmentBinding
import com.github.fragivity.example.viewbinding.viewBinding
import com.github.fragivity.navigator
import com.github.fragivity.popTo
import com.github.fragivity.push

class SearchFragment : Fragment(R.layout.search_fragment) {

    private val binding by viewBinding(SearchFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.input.setText(requireArguments().getString("keyword", ""))
        binding.btnGoToFeed.setOnClickListener {
            navigator.push("feed")
        }
        binding.btnGoBackFeed.setOnClickListener {
            navigator.popTo("feed")
        }
        binding.btnGoBackRoot.setOnClickListener {
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