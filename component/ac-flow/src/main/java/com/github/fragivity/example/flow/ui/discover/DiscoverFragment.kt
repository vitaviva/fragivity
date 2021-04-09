package com.github.fragivity.example.flow.ui.discover

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.fragivity.example.flow.R
import com.github.fragivity.example.flow.listener.OnFragmentOpenDrawerListener
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.flow_fragment_discover.*

class DiscoverFragment : Fragment(R.layout.flow_fragment_discover) {

    private val mToolbar get() = toolbar
    private val mTabLayout get() = tab_layout
    private val mViewPager get() = viewPager

    private var openDrawerListener: OnFragmentOpenDrawerListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentOpenDrawerListener) {
            openDrawerListener = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mToolbar.setTitle(R.string.discover)
        mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp)
        mToolbar.setNavigationOnClickListener {
            openDrawerListener?.onOpenDrawer()
        }

        mViewPager.adapter = DiscoverFragmentAdapter(this)
        TabLayoutMediator(mTabLayout, mViewPager) { tab, position ->
            tab.setText(
                when (position) {
                    0 -> R.string.recommend
                    1 -> R.string.hot
                    else -> R.string.favorite
                }
            )
        }.attach()
    }

    override fun onDetach() {
        super.onDetach()
        openDrawerListener = null
    }

    class DiscoverFragmentAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 3
        override fun createFragment(position: Int): Fragment {
            return PagerChildFragment.newInstance(position)
        }
    }
}