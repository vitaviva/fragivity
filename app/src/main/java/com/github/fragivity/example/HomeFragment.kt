package com.github.fragivity.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.fragivity.applySlideInOut
import com.github.fragivity.dialog.showDialog
import com.github.fragivity.example.backpress.BackPressFragment
import com.github.fragivity.example.communicate.CommFragment
import com.github.fragivity.example.deeplink.sendNotification
import com.github.fragivity.example.dialog.DialogFragment
import com.github.fragivity.example.launchmode.LaunchModeFragment
import com.github.fragivity.example.listscreen.Leaderboard
import com.github.fragivity.example.nested.NestedFragment
import com.github.fragivity.example.swipeback.SwipeBackFragment
import com.github.fragivity.finish
import com.github.fragivity.navigator
import com.github.fragivity.push
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : AbsBaseFragment(false) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_launchmode.setOnClickListener {
            navigator.push(LaunchModeFragment::class)
        }

        btn_transition.setOnClickListener {
            navigator.push(Leaderboard::class)
        }

        btn_deeplink.setOnClickListener {
            context?.sendNotification()
            finish()
        }

        btn_backpress.setOnClickListener {
            navigator.push(BackPressFragment::class)
        }

        btn_communication.setOnClickListener {
            navigator.push(CommFragment::class)
        }

        btn_swipeback.setOnClickListener {
            navigator.push(SwipeBackFragment::class) {
                applySlideInOut()
            }
        }

        btn_dialog.setOnClickListener {
            navigator.showDialog(DialogFragment::class)
        }

        btn_nested.setOnClickListener {
            navigator.push(NestedFragment::class)
        }
    }

    override var titleName: String?
        get() = "Fragivity"
        set(value) {}

}