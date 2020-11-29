package com.github.fragivity.communicate.callback

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.fragivity.AbsBaseFragment
import com.github.fragivity.communicate.CheckListAdapter
import com.github.fragivity.communicate.Item
import com.github.fragivity.communicate.originList
import com.github.fragivity.push
import com.my.example.R
import kotlinx.android.synthetic.main.fragivity_comm_list.*

class CheckListFragment : AbsBaseFragment() {

    private val _list = originList
    private val _cb: (Int, Boolean) -> Unit = { id, checked ->
        _adapter.submitList(_list.mapIndexed { index: Int, item: Item ->
            if (index == id) item.copy(checked = checked)
            else item
        })
    }
    private val _adapter by lazy {
        CheckListAdapter { id, checked ->
            push {
                CheckItemFragment(id, checked, _cb)
            }
        }
    }

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
