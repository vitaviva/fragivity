package com.github.fragivity.example.flow.ui.swipeback

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.fragivity.applyHorizontalInOut
import com.github.fragivity.example.base.OnItemClickListener
import com.github.fragivity.example.base.initToolbarNav
import com.github.fragivity.example.flow.R
import com.github.fragivity.example.flow.databinding.FlowFragmentSwipeBackRecyBinding
import com.github.fragivity.example.flow.ui.discover.PagerChildFragment
import com.github.fragivity.navigator
import com.github.fragivity.push
import com.github.fragivity.swipeback.attachToSwipeBack

class RecyclerSwipeBackFragment : Fragment() {

    private var _binding: FlowFragmentSwipeBackRecyBinding? = null
    private val binding get() = _binding!!

    private val mToolbar get() = binding.content.toolbar
    private val mRecyclerView get() = binding.recy

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FlowFragmentSwipeBackRecyBinding.inflate(inflater, container, false)
        return attachToSwipeBack(binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mToolbar.title = "SwipeBackActivity's Fragment"
        initToolbarNav(mToolbar)

        val adapter = PagerChildFragment.PagerAdapter()
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(view: View, item: Any, position: Int) {
                navigator.push(FirstSwipeBackFragment::class) {
                    applyHorizontalInOut()
                }
            }
        })
        adapter.submitList(createData(0))

        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        mRecyclerView.adapter = adapter
    }

    private fun createData(from: Int): List<String> {
        return List(20) {
            when (from) {
                0 -> getString(R.string.recommend) + " " + it
                1 -> getString(R.string.hot) + " " + it
                2 -> getString(R.string.favorite) + " " + it
                else -> ""
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}