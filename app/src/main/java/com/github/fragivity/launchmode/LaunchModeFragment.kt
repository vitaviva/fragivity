package com.github.fragivity.launchmode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.github.fragivity.*
import com.my.example.R
import kotlinx.android.synthetic.main.fragivity_launch_mode.*

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

        btn_standard.setOnClickListener {
            val bundle = bundleOf(FirstFirstFragment.ARGUMENTS_FROM to "Launch with Standard")
            push(LaunchModeFragment::class, bundle) {
                applySlideInOut()
            }
        }

        btn_singletop.setOnClickListener {
            val bundle = bundleOf(FirstFirstFragment.ARGUMENTS_FROM to "Launch with SingleTop")
            push(LaunchModeFragment::class, bundle) {
                launchMode = LaunchMode.SINGLE_TOP
                applySlideInOut()
            }
        }

        btn_singletask.setOnClickListener {
            val bundle = bundleOf(FirstFirstFragment.ARGUMENTS_FROM to "Launch with SingleTask")
            push {
                ToNextFragment {
                    push(LaunchModeFragment::class, bundle) {
                        launchMode = LaunchMode.SINGLE_TASK
                        applySlideInOut()
                    }
                }
            }
        }

        btn_poptohome.setOnClickListener {
            popTo(HomeFragment::class)
        }
    }

    override val titleName: String?
        get() = "Launch Mode"

}