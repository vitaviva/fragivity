package com.github.fragivity.example.communicate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.fragivity.example.AbsBaseFragment
import com.github.fragivity.example.R
import com.github.fragivity.navigator
import com.github.fragivity.push
import kotlinx.android.synthetic.main.fragment_comm.*


class CommFragment : AbsBaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_comm, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_viewmodel.setOnClickListener {
            navigator.push(com.github.fragivity.example.communicate.viewmodel.CheckListFragment::class)
        }

        btn_resultapi.setOnClickListener {
            navigator.push(com.github.fragivity.example.communicate.resultapi.CheckListFragment::class)
        }

        btn_callback.setOnClickListener {
            navigator.push(com.github.fragivity.example.communicate.callback.CheckListFragment::class)
        }
    }

    override val titleName: String?
        get() = "Communication"
}
