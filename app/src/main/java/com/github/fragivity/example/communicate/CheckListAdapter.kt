package com.github.fragivity.example.communicate

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.parcel.Parcelize

class CheckListAdapter(
    private val onItemClick: (id: Int, check: Boolean) -> Unit
) : ListAdapter<Item, CheckListAdapter.ViewHolder>(DIFF_CALLBACK) {

    internal var datas : List<Item>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(android.R.layout.simple_list_item_checked, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.findViewById<CheckedTextView>(android.R.id.text1).run {
            val item = getItem(position)
            text = "Item ${item.id} "
            isChecked = item.checked
            setOnClickListener {
                onItemClick(item.id, item.checked)
            }
        }
    }

    override fun submitList(list: List<Item>?) {
        super.submitList(list)
        datas = list
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem == newItem
            }
        }
    }
}

@Parcelize
data class Item(val id: Int, val checked: Boolean = false) : Parcelable

val originList = (0..49).map { Item(it) }