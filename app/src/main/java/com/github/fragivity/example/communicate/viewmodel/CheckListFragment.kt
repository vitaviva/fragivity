package com.github.fragivity.example.communicate.viewmodel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.fragivity.applyArguments
import com.github.fragivity.example.AbsBaseFragment
import com.github.fragivity.example.R
import com.github.fragivity.example.communicate.CheckListAdapter
import com.github.fragivity.example.communicate.Item
import com.github.fragivity.navigator
import com.github.fragivity.push
import kotlinx.android.synthetic.main.fragment_comm_list.*

class CheckListFragment : AbsBaseFragment() {
    private val _viewModel: ListViewModel by activityViewModels()
    private val _adapter by lazy {
        CheckListAdapter { id, checked ->
            navigator.push(CheckItemFragment::class) {
                applyArguments(
                    CheckItemFragment.ARGUMENTS_ID to id,
                    CheckItemFragment.ARGUMENTS_CHECKED to checked
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_comm_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(recycler) {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = _adapter
        }

        _viewModel.liveData.observe(this.viewLifecycleOwner, object : Observer<List<Item>> {
            override fun onChanged(t: List<Item>?) {
                _adapter.submitList(t)
            }
        })
    }

    override val titleName: String?
        get() = "Communication"
}
