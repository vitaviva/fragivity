package com.github.fragivity.communicate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.fragivity.AbsBaseFragment
import com.github.fragivity.push
import com.my.example.R
import kotlinx.android.synthetic.main.fragivity_comm.*


class CommFragment : AbsBaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragivity_comm, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_viewmodel.setOnClickListener {
            push(com.github.fragivity.communicate.viewmodel.CheckListFragment::class)
        }

        btn_resultapi.setOnClickListener {
            push(com.github.fragivity.communicate.resultapi.CheckListFragment::class)
        }

        btn_callback.setOnClickListener {
            push(com.github.fragivity.communicate.callback.CheckListFragment::class)
        }
    }
}
