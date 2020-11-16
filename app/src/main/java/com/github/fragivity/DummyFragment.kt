package com.github.fragivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.my.example.R
import kotlinx.android.synthetic.main.freenav_fragment_first_first.back
import kotlinx.android.synthetic.main.freenav_fragment_dummy.*

/**
 * @author wangpeng.rocky@bytedance.com
 */
class DummyFragment(
    private val _title: String = "DummyFragment",
    private val _next: DummyFragment.() -> Unit = {}
) : AbsBaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.freenav_fragment_dummy, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        back.setOnClickListener {
            pop()
        }

        next.setOnClickListener { _next.invoke(this) }

        title.text = _title
    }
}