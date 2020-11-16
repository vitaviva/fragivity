package com.github.fragivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.my.example.R
import kotlinx.android.synthetic.main.freenav_fragment_home.*

/**
 * @author wangpeng.rocky@bytedance.com
 */
class HomeFragment : AbsBaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.freenav_fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_standard.setOnClickListener {
            push(FirstFragment::class)
        }

        btn_singletop.setOnClickListener {
            push(SecondFragment::class)
        }

    }

}