package com.github.fragivity.example.communicate.resultapi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.fragivity.applyArguments
import com.github.fragivity.example.AbsBaseFragment
import com.github.fragivity.example.R
import com.github.fragivity.example.communicate.CheckListAdapter
import com.github.fragivity.example.communicate.Item
import com.github.fragivity.example.communicate.originList
import com.github.fragivity.example.communicate.resultapi.CheckItemFragment.Companion.REQUEST_KEY
import com.github.fragivity.example.communicate.resultapi.CheckItemFragment.Companion.RESULT_KEY
import com.github.fragivity.example.databinding.FragmentCommListBinding
import com.github.fragivity.example.viewbinding.viewBinding
import com.github.fragivity.navigator
import com.github.fragivity.push
import com.github.fragivity.resultapi.setFragmentResultListener

class CheckListFragment : AbsBaseFragment() {

    private val _list = originList
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

    private val binding by viewBinding(FragmentCommListBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requireActivity().supportFragmentManager.setFragmentResultListener(REQUEST_KEY, this, FragmentResultListener { requestKey, bundle ->
//            val result = checkNotNull(bundle.getParcelable<Item>(RESULT_KEY))
//            _adapter.submitList(_list.mapIndexed { index: Int, item: Item ->
//                if (index == result.id) result
//                else item
//            })
//        })
        setFragmentResultListener(REQUEST_KEY) { requestKey, bundle ->
            val result = checkNotNull(bundle.getParcelable<Item>(RESULT_KEY))
            _adapter.submitList(_list.mapIndexed { index: Int, item: Item ->
                if (index == result.id) result
                else item
            })
        }
    }

    override val titleName: String
        get() = "Communication"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_comm_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.recycler) {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = _adapter.apply {
                submitList(_list)
            }
        }

    }
}
