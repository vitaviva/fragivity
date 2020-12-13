package com.github.fragivity.example.swipeback

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.fragivity.example.AbsBaseFragment
import com.github.fragivity.swipeback.swipeBackLayout
import com.github.fragivity.example.R
import kotlinx.android.synthetic.main.fragment_swipe_back.*


class SwipeBackFragment : AbsBaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_swipe_back, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipe_enable.setOnCheckedChangeListener { _, checkedId ->
            swipeBackLayout.setEnableGesture(checkedId == R.id.swipe_on)
        }
    }

    override val titleName: String?
        get() = "Swipe Back"
}
