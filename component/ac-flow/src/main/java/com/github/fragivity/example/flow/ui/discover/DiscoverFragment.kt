package com.github.fragivity.example.flow.ui.discover

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.fragivity.example.flow.R
import com.github.fragivity.example.flow.databinding.FlowFragmentDiscoverBinding
import com.github.fragivity.example.flow.listener.OnFragmentOpenDrawerListener
import com.github.fragivity.example.viewbinding.viewBinding
import com.google.android.material.tabs.TabLayoutMediator

class DiscoverFragment : Fragment(R.layout.flow_fragment_discover) {

    private val binding by viewBinding(FlowFragmentDiscoverBinding::bind)

    private val mToolbar get() = binding.toolbar
    private val mTabLayout get() = binding.tabLayout
    private val mViewPager get() = binding.viewPager

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