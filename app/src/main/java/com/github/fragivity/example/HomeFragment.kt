package com.github.fragivity.example

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.github.fragivity.applySlideInOut
import com.github.fragivity.dialog.showDialog
import com.github.fragivity.example.backpress.BackPressFragment
import com.github.fragivity.example.communicate.CommFragment
import com.github.fragivity.example.databinding.FragmentHomeBinding
import com.github.fragivity.example.deeplink.sendNotification
import com.github.fragivity.example.dialog.DialogFragment
import com.github.fragivity.example.flow.ui.MainActivity
import com.github.fragivity.example.launchmode.LaunchModeFragment
import com.github.fragivity.example.listscreen.Leaderboard
import com.github.fragivity.example.nested.NestedFragment
import com.github.fragivity.example.swipeback.SwipeBackFragment
import com.github.fragivity.example.viewbinding.viewBinding
import com.github.fragivity.finish
import com.github.fragivity.navigator
import com.github.fragivity.push

class HomeFragment : AbsBaseFragment(false) {

    private val binding by viewBinding(FragmentHomeBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLaunchmode.setOnClickListener {
            navigator.push(LaunchModeFragment::class)
        }

        binding.btnTransition.setOnClickListener {
            navigator.push(Leaderboard::class)
        }

        binding.btnDeeplink.setOnClickListener {
            context?.sendNotification()
            finish()
        }

        binding.btnBackpress.setOnClickListener {
            navigator.push(BackPressFragment::class)
        }

        binding.btnCommunication.setOnClickListener {
            navigator.push(CommFragment::class)
        }

        binding.btnSwipeback.setOnClickListener {
            navigator.push(SwipeBackFragment::class) {
                applySlideInOut()
            }
        }

        binding.btnDialog.setOnClickListener {
            navigator.showDialog(DialogFragment::class)
        }

        binding.btnNested.setOnClickListener {
            navigator.push(NestedFragment::class)
        }

        binding.btnRouter.setOnClickListener {
            navigator.push("feed") {
                arguments = bundleOf("isShowBackSearch" to false)
            }
        }

        binding.btnGoToFlow.setOnClickListener {
            startActivity(Intent(requireContext(), MainActivity::class.java))
        }
    }

    override val titleName: String
        get() = "Fragivity"

}