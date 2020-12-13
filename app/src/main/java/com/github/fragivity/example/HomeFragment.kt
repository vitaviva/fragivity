package com.github.fragivity.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.fragivity.example.deeplink.sendNotification
import com.github.fragivity.example.backpress.BackPressFragment
import com.github.fragivity.example.communicate.CommFragment
import com.github.fragivity.example.dialog.DialogFragment
import com.github.fragivity.example.dialog.showDialog
import com.github.fragivity.example.launchmode.LaunchModeFragment
import com.github.fragivity.example.listscreen.Leaderboard
import com.github.fragivity.example.nested.NestedFragment
import com.github.fragivity.example.swipeback.SwipeBackFragment
import com.github.fragivity.finish
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
            push(LaunchModeFragment::class)
        }

        btn_transition.setOnClickListener {
            push(Leaderboard::class)
        }

        btn_deeplink.setOnClickListener {
            context?.sendNotification()
            finish()
        }

        btn_backpress.setOnClickListener {
            push(BackPressFragment::class)
        }

        btn_communication.setOnClickListener {
            push(CommFragment::class)
        }

        btn_swipeback.setOnClickListener {
            push(SwipeBackFragment::class)
        }

        btn_dialog.setOnClickListener {
            showDialog(DialogFragment::class)
        }

        btn_nested.setOnClickListener {
            push(NestedFragment::class)
        }
    }

    override var titleName: String?
        get() = "Fragivity"
        set(value) {}

}