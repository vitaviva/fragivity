package com.github.fragivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.github.fragivity.FirstFirstFragment.Companion.ARGUMENTS_FROM
import com.my.example.R
import kotlinx.android.synthetic.main.freenav_fragment_first.*

/**
 * @author wangpeng.rocky@bytedance.com
 */
class FirstFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.freenav_fragment_first, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val bundle = bundleOf(ARGUMENTS_FROM to this::class.java.simpleName)
        btn_first_first.setOnClickListener {
            push(FirstFirstFragment::class, bundle) {
                setEnterAnim(R.anim.nav_default_enter_anim)
                setExitAnim(R.anim.nav_default_exit_anim)
                setPopEnterAnim(R.anim.nav_default_enter_anim)
                setPopExitAnim(R.anim.nav_default_exit_anim)
            }
        }

        btn_first_second.setOnClickListener {
            push(FirstSecondFragment::class, bundle) {
                setEnterAnim(R.anim.slide_in)
                setExitAnim(R.anim.slide_out)
                setPopEnterAnim(R.anim.slide_in_pop)
                setPopExitAnim(R.anim.slide_out_pop)
            }
        }
    }

}