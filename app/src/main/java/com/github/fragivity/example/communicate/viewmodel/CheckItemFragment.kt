package com.github.fragivity.example.communicate.viewmodel

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.github.fragivity.example.AbsBaseFragment
import com.github.fragivity.example.R
import com.github.fragivity.example.databinding.FragmentCommItemBinding
import com.github.fragivity.example.viewbinding.viewBinding

class CheckItemFragment : AbsBaseFragment() {
    private val viewModel: ListViewModel by activityViewModels()

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
        val _id = requireArguments().getInt(ARGUMENTS_ID).also { binding.name.text = "Item $it" }
        // val _check = requireArguments().getBoolean(ARGUMENTS_CHECKED).also { binding.checkbox.isChecked = it }
        binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateCheckState(_id, isChecked)
        }
    }

    override val titleName: String
        get() = "Communication"

    companion object {
        const val ARGUMENTS_ID = "id"
        const val ARGUMENTS_CHECKED = "checked"
    }
}
