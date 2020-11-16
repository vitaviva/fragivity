package com.github.fragivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.my.example.R
import kotlinx.android.synthetic.main.freenav_fragment_second.*

/**
 * @author wangpeng.rocky@bytedance.com
 */
class SecondFragment : AbsBaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.freenav_fragment_second, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn.setOnClickListener {
            push(SecondFragment::class) {
                setLaunchSingleTop(true)
            }
//            push {
//                DummyFragment("from : ${this.javaClass.simpleName}") {
//                    push(SecondFragment::class) {
//                        setLaunchSingleTop(true)
//                    }
//                }
//            }
        }
    }
}