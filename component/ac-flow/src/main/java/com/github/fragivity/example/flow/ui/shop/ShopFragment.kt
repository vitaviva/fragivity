package com.github.fragivity.example.flow.ui.shop

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import com.github.fragivity.*
import com.github.fragivity.example.flow.R
import com.github.fragivity.example.flow.listener.OnFragmentOpenDrawerListener
import kotlinx.android.synthetic.main.flow_content_toolbar.*

class ShopFragment : Fragment(R.layout.flow_fragment_shop) {

    private val mToolbar get() = toolbar

    private lateinit var contentNavigator: FragivityNavHost

    private var openDrawerListener: OnFragmentOpenDrawerListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentOpenDrawerListener) {
            openDrawerListener = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().supportFragmentManager
            .setFragmentResultListener(KEY_SWITCH_MENU, this, FragmentResultListener { _, result ->
                switchContentFragment(result.getString(ARGS_MENU, ""))
            })

        mToolbar.setTitle(R.string.shop)
        mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp)
        mToolbar.setNavigationOnClickListener {
            openDrawerListener?.onOpenDrawer()
        }

        val menus = resources.getStringArray(R.array.array_menu)

        val mMenuListNavHostFragment = findNavHostFragment(R.id.fl_list_container)
        mMenuListNavHostFragment.loadRoot { MenuListFragment.newInstance(menus) }

        val mContentNavHostFragment = findNavHostFragment(R.id.fl_content_container)
        mContentNavHostFragment.loadRoot { ContentFragment.newInstance(menus[0]) }
        contentNavigator = mContentNavHostFragment.navigator
    }

    private fun switchContentFragment(menu: String) {
        contentNavigator.push(ContentFragment::class) {
            launchMode = LaunchMode.SINGLE_TASK
            applyArguments(ContentFragment.ARGS_MENU to menu)
        }
    }

    override fun onDetach() {
        super.onDetach()
        openDrawerListener = null
    }

    companion object {
        const val ARGS_MENU = "shop_args_menu"
        const val KEY_SWITCH_MENU = "shop_key_switch_menu"
    }
}