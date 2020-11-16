package com.github.fragivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.my.example.R
import kotlinx.android.synthetic.main.freenav_fragment_first_first.*
import java.lang.RuntimeException

/**
 * @author wangpeng.rocky@bytedance.com
 */
class FirstFirstFragment : AbsBaseFragment() {

    companion object {
        const val ARGUMENTS_FROM = "from"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.freenav_fragment_first_first, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        back.setOnClickListener {
            pop()
        }

        val name = arguments?.getString(ARGUMENTS_FROM) ?: throw RuntimeException("")
        from.text = name
    }
}