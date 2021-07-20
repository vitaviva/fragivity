package com.github.fragivity.example.launchmode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.github.fragivity.*
import com.github.fragivity.example.AbsBaseFragment
import com.github.fragivity.example.HomeFragment
import com.github.fragivity.example.R
import com.github.fragivity.example.databinding.FragmentLaunchModeBinding
import com.github.fragivity.example.viewbinding.viewBinding

class LaunchModeFragment : AbsBaseFragment() {

    private val binding by viewBinding(FragmentLaunchModeBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_launch_mode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnStandard.setOnClickListener {
            navigator.push(LaunchModeFragment::class) {
                applyArguments(ARGUMENTS_FROM to "Launch with Standard")
            }
        }

        binding.btnSingletop.setOnClickListener {
            navigator.push(LaunchModeFragment::class) {
                applySlideInOut()
                applyLaunchMode(LaunchMode.SINGLE_TOP)
                applyArguments(ARGUMENTS_FROM to "Launch with SingleTop")
            }
        }

        binding.btnSingletask.setOnClickListener {
            navigator.push {
                ToNextFragment {
                    navigator.push(LaunchModeFragment::class) {
                        applyLaunchMode(LaunchMode.SINGLE_TASK)
                        applyArguments(ARGUMENTS_FROM to "Launch with SingleTask")
                    }
                }
            }
        }

        binding.btnPoptohome.setOnClickListener {
            navigator.popTo(HomeFragment::class)
        }

        binding.btnPushtohome.setOnClickListener {
            navigator.pushTo(HomeFragment::class)
        }
    }

    override val titleName: String
        get() = arguments?.getString(ARGUMENTS_FROM) ?: "Launch Mode"

}

const val ARGUMENTS_FROM = "from"