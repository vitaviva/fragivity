package com.github.fragivity.example.launchmode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.github.fragivity.LaunchMode
import com.github.fragivity.example.AbsBaseFragment
import com.github.fragivity.example.HomeFragment
import com.github.fragivity.example.R
import com.github.fragivity.example.applySlideInOut
import com.github.fragivity.navigator
import com.github.fragivity.popTo
import com.github.fragivity.push
import kotlinx.android.synthetic.main.fragment_launch_mode.*

class LaunchModeFragment : AbsBaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_launch_mode, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_standard.setOnClickListener {
            val bundle = bundleOf(ARGUMENTS_FROM to "Launch with Standard")
            navigator.push(LaunchModeFragment::class, bundle) {
                applySlideInOut()
            }
        }

        btn_singletop.setOnClickListener {
            val bundle = bundleOf(ARGUMENTS_FROM to "Launch with SingleTop")
            navigator.push(LaunchModeFragment::class, bundle) {
                launchMode = LaunchMode.SINGLE_TOP
                applySlideInOut()
            }
        }

        btn_singletask.setOnClickListener {
            val bundle = bundleOf(ARGUMENTS_FROM to "Launch with SingleTask")
            navigator.push {
                ToNextFragment {
                    navigator.push(LaunchModeFragment::class, bundle) {
                        launchMode = LaunchMode.SINGLE_TASK
                        applySlideInOut()
                    }
                }
            }
        }

        btn_poptohome.setOnClickListener {
            navigator.popTo(HomeFragment::class)
        }
    }

    override val titleName: String?
        get() = arguments?.getString(ARGUMENTS_FROM) ?: "Launch Mode"

}

const val ARGUMENTS_FROM = "from"