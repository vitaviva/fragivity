package com.github.fragivity.example.communicate.callback

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.fragivity.example.AbsBaseFragment
import com.github.fragivity.example.R
import kotlinx.android.synthetic.main.fragment_comm_item.*

class CheckItemFragment(
    private val _id: Int,
    private val _check: Boolean,
    private val _cb: (id: Int, check: Boolean) -> Unit
) : AbsBaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_comm_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        name.text = "Item ${_id}"
        checkbox.isChecked = _check
        checkbox.setOnCheckedChangeListener { _, isChecked ->
            _cb(_id, isChecked)
        }
    }

    override val titleName: String?
        get() = "Communication"
}

