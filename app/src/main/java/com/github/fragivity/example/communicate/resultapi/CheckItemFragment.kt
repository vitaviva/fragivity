package com.github.fragivity.example.communicate.resultapi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.github.fragivity.example.AbsBaseFragment
import com.github.fragivity.example.R
import com.github.fragivity.example.communicate.Item
import com.github.fragivity.resultapi.setFragmentResult
import kotlinx.android.synthetic.main.fragment_comm_item.*

class CheckItemFragment : AbsBaseFragment() {

    private var _id: Int = 0
    private var _check: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_comm_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _id = requireArguments().getInt(ARGUMENTS_ID).also { name.text = "Item ${it}" }
        _check = requireArguments().getBoolean(ARGUMENTS_CHECKED).also { checkbox.isChecked = it }
        checkbox.setOnCheckedChangeListener { _, isChecked ->
            _check = isChecked
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        requireActivity().supportFragmentManager.setFragmentResult(REQUEST_KEY, bundleOf(RESULT_KEY to Item(_id, _check)))
        setFragmentResult(REQUEST_KEY, bundleOf(RESULT_KEY to Item(_id, _check)))
    }

    override val titleName: String?
        get() = "Communication"

    companion object {
        const val ARGUMENTS_ID = "id"
        const val ARGUMENTS_CHECKED = "checked"
        const val REQUEST_KEY = "requestKey"
        const val RESULT_KEY = "bundleKey"
    }
}

