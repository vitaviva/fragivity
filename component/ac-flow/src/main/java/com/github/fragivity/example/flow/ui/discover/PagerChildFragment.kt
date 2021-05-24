package com.github.fragivity.example.flow.ui.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.fragivity.applyVerticalInOut
import com.github.fragivity.example.base.OnItemClickListener
import com.github.fragivity.example.base.doOnIdle
import com.github.fragivity.example.flow.R
import com.github.fragivity.example.flow.databinding.FlowFragmentPagerBinding
import com.github.fragivity.example.ui.CycleFragment
import com.github.fragivity.example.viewbinding.viewBinding
import com.github.fragivity.navigator
import com.github.fragivity.push

class PagerChildFragment : Fragment(R.layout.flow_fragment_pager) {

    private val binding by viewBinding(FlowFragmentPagerBinding::bind)

    private val mRecyclerView get() = binding.recy

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PagerAdapter()
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(view: View, item: Any, position: Int) {
                navigator.push(CycleFragment::class) {
                    applyVerticalInOut()
                }
            }
        })

        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        mRecyclerView.adapter = adapter

        doOnIdle {
            adapter.submitList(createData(requireArguments().getInt(ARGS_FROM)))
        }
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

    class PagerAdapter : ListAdapter<String, PagerAdapter.ViewHolder>(DIFF_CALLBACK) {

        private var mListener: OnItemClickListener? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.flow_item_pager, parent, false
            )
            val holder = ViewHolder(view)
            view.setOnClickListener {
                val position = holder.layoutPosition
                mListener?.onItemClick(view, getItem(position), position)
            }
            return holder
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.tvTitle.text = getItem(position)
        }

        fun setOnItemClickListener(l: OnItemClickListener?) {
            mListener = l
        }

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvTitle: TextView = view.findViewById(R.id.tv_title)
        }

        companion object {
            private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
                override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
                    oldItem.hashCode() == newItem.hashCode()

                override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
                    oldItem == newItem
            }
        }
    }

    companion object {
        private const val ARGS_FROM = "pager_child_args_from"
        fun newInstance(position: Int) = PagerChildFragment().apply {
            arguments = bundleOf(ARGS_FROM to position)
        }
    }
}