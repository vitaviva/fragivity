package com.github.fragivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.my.example.R
import kotlinx.android.synthetic.main.fragivity_launch_mode.*

/**
 * @author wangpeng.rocky@bytedance.com
 */
class LaunchModeFragment : AbsBaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragivity_launch_mode, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.getString(FirstFirstFragment.ARGUMENTS_FROM)?.let { title.text = it }

        btn_playground.setOnClickListener {
            val bundle = bundleOf(FirstFirstFragment.ARGUMENTS_FROM to "Launched by Standard")
            push(LaunchModeFragment::class, bundle) {
                applySlideInOut()
            }
        }

        btn_launchmode.setOnClickListener {
            val bundle = bundleOf(FirstFirstFragment.ARGUMENTS_FROM to "Launched by SingleTop")
            push(LaunchModeFragment::class, bundle) {
                setLaunchSingleTop(true)
                applySlideInOut()
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