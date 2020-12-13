package com.github.fragivity.example.communicate.viewmodel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.github.fragivity.example.AbsBaseFragment
import com.github.fragivity.example.R
import kotlinx.android.synthetic.main.fragment_comm_item.*

class CheckItemFragment : AbsBaseFragment() {
    private val viewModel: ListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_comm_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val _id =
            requireArguments().getInt(ARGUMENTS_ID).also { name.text = "Item ${it}" }
        val _check =
            requireArguments().getBoolean(ARGUMENTS_CHECKED).also { checkbox.isChecked = it }
        checkbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateCheckState(_id, isChecked)
        }
    }

    override val titleName: String?
        get() = "Communication"

    companion object {
        const val ARGUMENTS_ID = "id"
        const val ARGUMENTS_CHECKED = "checked"
    }
}
