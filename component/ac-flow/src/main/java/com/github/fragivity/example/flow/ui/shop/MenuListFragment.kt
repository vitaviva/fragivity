package com.github.fragivity.example.flow.ui.shop

import android.graphics.Color
import android.os.Bundle
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.fragivity.example.base.OnItemClickListener
import com.github.fragivity.example.base.setTextColorRes
import com.github.fragivity.example.flow.R
import kotlinx.android.synthetic.main.flow_fragment_list_menu.*
import kotlinx.android.synthetic.main.flow_item_menu.view.*

class MenuListFragment : Fragment(R.layout.flow_fragment_list_menu) {

    private val mRecyclerView get() = recy

    private lateinit var mAdapter: MenuAdapter
    private var mCurrentPosition = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        mAdapter = MenuAdapter(requireArguments().getStringArray(ARGS_MENUS) ?: emptyArray())
        mAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(view: View, item: Any, position: Int) {
                if (item !is String) return
                showContent(position, item)
            }
        })
        mRecyclerView.adapter = mAdapter

        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(SAVE_STATE_POSITION)
        }
        mAdapter.setItemChecked(mCurrentPosition)
    }

    private fun showContent(position: Int, menu: String) {
        if (position == mCurrentPosition) return

        mCurrentPosition = position
        mAdapter.setItemChecked(position)

        requireActivity().supportFragmentManager
            .setFragmentResult(
                ShopFragment.KEY_SWITCH_MENU,
                bundleOf(ShopFragment.ARGS_MENU to menu)
            )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SAVE_STATE_POSITION, mCurrentPosition)
    }

    class MenuAdapter(
        private val items: Array<String>
    ) : RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

        private val mBooleanArray = SparseBooleanArray(items.size)
        private var mLastCheckedPosition = -1

        private var mListener: OnItemClickListener? = null

        fun setItemChecked(position: Int) {
            mBooleanArray.put(position, true)
            if (mLastCheckedPosition > -1) {
                mBooleanArray.put(mLastCheckedPosition, false)
                notifyItemChanged(mLastCheckedPosition, false)
            }
            notifyItemChanged(position, true)
            mLastCheckedPosition = position
        }

        override fun getItemCount(): Int = items.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.flow_item_menu, parent, false
            )
            val holder = ViewHolder(view)
            holder.itemView.setOnClickListener {
                val position = holder.layoutPosition
                mListener?.onItemClick(view, items[position], position)
            }
            return holder
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.setChecked(mBooleanArray[position])
            holder.tvName.text = items[position]
        }

        override fun onBindViewHolder(
            holder: ViewHolder,
            position: Int,
            payloads: MutableList<Any>
        ) {
            if (payloads.isEmpty()) {
                onBindViewHolder(holder, position)
            } else {
                val isChecked = payloads[0] as? Boolean ?: return
                holder.setChecked(isChecked)
            }
        }

        fun setOnItemClickListener(l: OnItemClickListener?) {
            mListener = l
        }

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val viewLine: View = view.view_line
            val tvName: TextView = view.tv_name

            fun setChecked(isChecked: Boolean) {
                if (isChecked) {
                    viewLine.isVisible = true
                    itemView.setBackgroundColor(Color.WHITE)
                    tvName.setTextColorRes(R.color.colorAccent)
                } else {
                    viewLine.isInvisible = true
                    itemView.setBackgroundResource(R.color.bg_app)
                    tvName.setTextColor(Color.BLACK)
                }
            }
        }
    }

    companion object {
        const val SAVE_STATE_POSITION = "menu_list_save_state_position"
        private const val ARGS_MENUS = "menu_list_args_menus"
        fun newInstance(menus: Array<String>) = MenuListFragment().apply {
            arguments = bundleOf(ARGS_MENUS to menus)
        }
    }
}