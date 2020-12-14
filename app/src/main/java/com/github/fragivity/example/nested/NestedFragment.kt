package com.github.fragivity.example.nested

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.github.fragivity.*
import com.github.fragivity.example.AbsBaseFragment
import com.github.fragivity.example.HomeFragment
import com.github.fragivity.example.R


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

        navHostFragment.loadRoot(HomeFragment::class)
    }

    override val titleName: String?
        get() = "Nested"
}
