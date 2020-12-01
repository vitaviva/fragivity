package com.github.fragivity.nested

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.github.fragivity.*
import com.my.example.R
import kotlinx.android.synthetic.main.fragivity_swipe_back.*
import kotlinx.android.synthetic.main.fragment_nested.*
import kotlinx.android.synthetic.main.freenav_fragment_home.*


class NestedFragment : AbsBaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nested, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHostFragment = childFragmentManager
            .findFragmentById(R.id.nav_nested_host) as NavHostFragment

        navHostFragment.loadRoot(HomeFragment::class, R.id.nav_nested_host)
    }
}
