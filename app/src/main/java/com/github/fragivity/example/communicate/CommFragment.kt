package com.github.fragivity.example.communicate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.fragivity.example.AbsBaseFragment
import com.github.fragivity.example.R
import com.github.fragivity.example.databinding.FragmentCommBinding
import com.github.fragivity.example.viewbinding.viewBinding
import com.github.fragivity.navigator
import com.github.fragivity.push


class CommFragment : AbsBaseFragment() {

    private val binding by viewBinding(FragmentCommBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_comm, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnViewmodel.setOnClickListener {
            navigator.push(com.github.fragivity.example.communicate.viewmodel.CheckListFragment::class)
        }

        binding.btnResultapi.setOnClickListener {
            navigator.push(com.github.fragivity.example.communicate.resultapi.CheckListFragment::class)
        }

        binding.btnCallback.setOnClickListener {
            navigator.push(com.github.fragivity.example.communicate.callback.CheckListFragment::class)
        }
    }

    override val titleName: String
        get() = "Communication"
}
