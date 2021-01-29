package com.github.fragivity.example.communicate.callback

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.fragivity.example.AbsBaseFragment
import com.github.fragivity.example.R
import com.github.fragivity.example.communicate.CheckListAdapter
import com.github.fragivity.example.communicate.Item
import com.github.fragivity.example.communicate.originList
import com.github.fragivity.navigator
import com.github.fragivity.push
import kotlinx.android.synthetic.main.fragment_comm_list.*

class CheckListFragment : AbsBaseFragment() {

    private val _cb: (Int, Boolean) -> Unit = { id, checked ->
        _adapter.datas?.let {
            _adapter.submitList(it.mapIndexed { index: Int, item: Item ->
                if (index == id) item.copy(checked = checked)
                else item
            })
        }

    }
    private val _adapter by lazy {
        CheckListAdapter { id, checked ->
            navigator.push {
                CheckItemFragment(id, checked, _cb)
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
            adapter = _adapter.apply {
                submitList(_adapter.datas ?: originList)
            }
        }

    }

    override val titleName: String?
        get() = "Communication"
}
