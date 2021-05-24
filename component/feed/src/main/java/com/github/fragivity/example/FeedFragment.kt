package com.github.fragivity.example

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.github.fragivity.example.feed.R
import com.github.fragivity.example.feed.databinding.FeedFragmentBinding
import com.github.fragivity.example.viewbinding.viewBinding
import com.github.fragivity.navigator
import com.github.fragivity.popTo
import com.github.fragivity.push
import kotlin.random.Random

class FeedFragment : Fragment(R.layout.feed_fragment) {

    private val binding by viewBinding(FeedFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnGoToSearch.setOnClickListener {
            val value = Random.Default.nextInt(100)
            if (value % 2 == 0) {
                navigator.push("search?keyword=$value")
            } else {
                navigator.push("search") {
                    arguments = bundleOf("keyword" to value.toString())
                }
            }
        }
        binding.btnGoBackSearch.setOnClickListener {
            navigator.popTo("search")
        }
        binding.btnGoBackSearch.isVisible = requireArguments().getBoolean("isShowBackSearch", true)
    }

    companion object {
        fun newInstance() = FeedFragment()
    }
}