package com.github.fragivity.communicate.resultapi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.fragivity.AbsBaseFragment
import com.github.fragivity.communicate.CheckListAdapter
import com.github.fragivity.communicate.Item
import com.github.fragivity.communicate.originList
import com.github.fragivity.communicate.resultapi.CheckItemFragment.Companion.REQUEST_KEY
import com.github.fragivity.communicate.resultapi.CheckItemFragment.Companion.RESULT_KEY
import com.github.fragivity.push
import com.github.fragivity.resultapi.setFragmentResultListener
import com.my.example.R
import kotlinx.android.synthetic.main.fragivity_comm_list.*

class CheckListFragment : AbsBaseFragment() {

    private val _list = originList
    private val _adapter by lazy {
        CheckListAdapter { id, checked ->
            push(
                CheckItemFragment::class,
                bundleOf(
                    CheckItemFragment.ARGUMENTS_ID to id,
                    CheckItemFragment.ARGUMENTS_CHECKED to checked
                )
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requireActivity().supportFragmentManager.setFragmentResultListener(REQUEST_KEY, this, FragmentResultListener { requestKey, bundle ->
//            val result = checkNotNull(bundle.getParcelable<Item>(RESULT_KEY))
//            _adapter.submitList(_list.mapIndexed { index: Int, item: Item ->
//                if (index == result.id) result
//                else item
//            })
//        })
        setFragmentResultListener(REQUEST_KEY)  { requestKey, bundle ->
            val result = checkNotNull(bundle.getParcelable<Item>(RESULT_KEY))
            _adapter.submitList(_list.mapIndexed { index: Int, item: Item ->
                if (index == result.id) result
                else item
            })
        }
    }

    override val titleName: String?
        get() = "Communication"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragivity_comm_list, container, false)
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
            adapter = _adapter.apply {
                submitList(_list)
            }
        }

    }
}
