package com.github.fragivity.example.flow.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.fragivity.applyArguments
import com.github.fragivity.applyVerticalInOut
import com.github.fragivity.example.base.OnItemClickListener
import com.github.fragivity.example.flow.R
import com.github.fragivity.example.flow.listener.OnFragmentOpenDrawerListener
import com.github.fragivity.navigator
import com.github.fragivity.push
import kotlinx.android.synthetic.main.flow_content_toolbar.*
import kotlinx.android.synthetic.main.flow_fragment_home.*
import kotlinx.android.synthetic.main.flow_item_home.view.*
import kotlin.random.Random

class HomeFragment : Fragment(R.layout.flow_fragment_home), Toolbar.OnMenuItemClickListener {

    private val mToolbar get() = toolbar
    private val mList get() = recy

    private var openDrawerListener: OnFragmentOpenDrawerListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentOpenDrawerListener) {
            openDrawerListener = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mToolbar.setTitle(R.string.home)
        mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp)
        mToolbar.setNavigationOnClickListener {
            openDrawerListener?.onOpenDrawer()
        }
        mToolbar.inflateMenu(R.menu.flow_home)
        mToolbar.setOnMenuItemClickListener(this)

        val adapter = HomeAdapter()
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(view: View, item: Any, position: Int) {
                if (item !is Article) return
                if (Random.nextBoolean()) {
                    navigator.push(DetailFragment::class) {
                        applyVerticalInOut()
                        applyArguments(DetailFragment.ARGS_TITLE to item.title + "_v1")
                    }
                } else {
                    navigator.push({
                        applyVerticalInOut()
                        applyArguments(DetailFragment.ARGS_TITLE to item.title + "_v2")
                    }, { args ->
                        val title = args.getString(DetailFragment.ARGS_TITLE)
                        DetailFragment.newInstance("$title*")
                    })
                }
            }
        })

        mList.setHasFixedSize(true)
        mList.layoutManager = LinearLayoutManager(requireContext())
        mList.adapter = adapter

        val titles = resources.getStringArray(R.array.array_title)
        val contents = resources.getStringArray(R.array.array_content)

        adapter.items = List(15) {
            val index = Random.Default.nextInt(3)
            Article(titles[index], contents[index])
        }
        adapter.notifyItemRangeChanged(0, adapter.itemCount)
    }

    override fun onDetach() {
        super.onDetach()
        openDrawerListener = null
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        // when (item.itemId) {
        //
        // }
        return true
    }

    class HomeAdapter : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

        var items: List<Article> = emptyList()

        private var listener: OnItemClickListener? = null

        override fun getItemCount(): Int = items.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.flow_item_home, parent, false)
            val holder = ViewHolder(view)
            view.setOnClickListener {
                val position = holder.layoutPosition
                listener?.onItemClick(view, items[position], position)
            }
            return holder
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.tvTitle.text = item.title
            holder.tvContent.text = item.content
        }

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvTitle: TextView = view.tv_title
            val tvContent: TextView = view.tv_content
        }

        fun setOnItemClickListener(l: OnItemClickListener?) {
            listener = l
        }
    }

    data class Article(
        val title: String,
        val content: String
    )
}