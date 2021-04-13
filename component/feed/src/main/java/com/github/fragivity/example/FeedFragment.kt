package com.github.fragivity.example

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.github.fragivity.example.feed.R
import com.github.fragivity.navigator
import com.github.fragivity.popTo
import com.github.fragivity.push
import kotlinx.android.synthetic.main.feed_fragment.*
import kotlin.random.Random

class FeedFragment : Fragment(R.layout.feed_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_go_to_search.setOnClickListener {
            val value = Random.Default.nextInt(100)
            if (value % 2 == 0) {
                navigator.push("search?keyword=$value")
            } else {
                navigator.push("search") {
                    arguments = bundleOf("keyword" to value.toString())
                }
            }
        }
        btn_go_back_search.setOnClickListener {
            navigator.popTo("search")
        }
        btn_go_back_search.isVisible = requireArguments().getBoolean("isShowBackSearch", true)
    }

    companion object {
        fun newInstance() = FeedFragment()
    }
}