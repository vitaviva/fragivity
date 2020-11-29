package com.github.fragivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.fragivity.backpress.BackPressFragment
import com.github.fragivity.communicate.CommFragment
import com.github.fragivity.communicate.viewmodel.CheckListFragment
import com.github.fragivity.deeplink.sendNotification
import com.github.fragivity.listscreen.Leaderboard
import com.github.fragivity.swipeback.SwipeBackFragment
import com.my.example.R
import kotlinx.android.synthetic.main.freenav_fragment_home.*

/**
 * @author wangpeng.rocky@bytedance.com
 */
class HomeFragment : AbsBaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.freenav_fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_playground.setOnClickListener {
            push(FirstFragment::class)
        }

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
    }

}