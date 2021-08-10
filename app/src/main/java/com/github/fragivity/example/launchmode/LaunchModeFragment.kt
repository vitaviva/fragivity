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

        val num = arguments?.getInt(ARGUMENTS_NUM) ?: 0

        binding.btnStandard.setOnClickListener {
            navigator.push(LaunchModeFragment::class) {
                applyArguments(
                    ARGUMENTS_NUM to num + 1
                )
            }
        }

        binding.btnSingletop.setOnClickListener {
            navigator.push(LaunchModeFragment::class) {
                applySlideInOut()
                applyLaunchMode(LaunchMode.SINGLE_TOP)
                applyArguments(
                    ARGUMENTS_FROM to "SingleTop",
                    ARGUMENTS_NUM to num
                )
            }
        }

        binding.btnSingletask.setOnClickListener {
            navigator.push {
                ToNextFragment {
                    navigator.push(LaunchModeFragment::class) {
                        applyLaunchMode(LaunchMode.SINGLE_TASK)
                        applyArguments(
                            ARGUMENTS_FROM to "SingleTask",
                            ARGUMENTS_NUM to num
                        )
                    }
                }
            }
        }

        binding.btnPopSelf.setOnClickListener {
            navigator.push(LaunchModeFragment::class) {
                applyArguments(
                    ARGUMENTS_NUM to num + 1
                )
                popSelf = true
            }
        }

        binding.btnPoptohome.setOnClickListener {
            navigator.popTo(HomeFragment::class)
        }

        binding.btnPushtohome.setOnClickListener {
            navigator.pushTo(HomeFragment::class) {
                applySlideInOut()
            }
        }
    }

    override val titleName: String
        get() = "Launch ${arguments?.getString(ARGUMENTS_FROM, "Standard")} (${
            arguments?.getInt(
                ARGUMENTS_NUM,
                0
            )
        })"

}

const val ARGUMENTS_FROM = "from"
const val ARGUMENTS_NUM = "num"