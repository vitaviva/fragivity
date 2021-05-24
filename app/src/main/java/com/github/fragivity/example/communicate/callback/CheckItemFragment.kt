package com.github.fragivity.example.communicate.callback

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.fragivity.example.AbsBaseFragment
import com.github.fragivity.example.R
import com.github.fragivity.example.databinding.FragmentCommItemBinding
import com.github.fragivity.example.viewbinding.viewBinding

class CheckItemFragment(
    private val _id: Int,
    private val _check: Boolean,
    private val _cb: (id: Int, check: Boolean) -> Unit
) : AbsBaseFragment() {

    private val binding by viewBinding(FragmentCommItemBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_comm_item, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.name.text = "Item $_id"
        binding.checkbox.isChecked = _check
        binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            _cb(_id, isChecked)
        }
    }

    override val titleName: String
        get() = "Communication"
}

