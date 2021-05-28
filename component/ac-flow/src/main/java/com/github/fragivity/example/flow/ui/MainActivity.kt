package com.github.fragivity.example.flow.ui

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.proxyFragmentFactory
import com.github.fragivity.*
import com.github.fragivity.debug.showDebugView
import com.github.fragivity.example.base.showToast
import com.github.fragivity.example.flow.R
import com.github.fragivity.example.flow.databinding.FlowActivityMainBinding
import com.github.fragivity.example.flow.listener.OnFragmentOpenDrawerListener
import com.github.fragivity.example.flow.listener.OnLoginSuccessListener
import com.github.fragivity.example.flow.ui.account.LoginFragment
import com.github.fragivity.example.flow.ui.discover.DiscoverFragment
import com.github.fragivity.example.flow.ui.home.HomeFragment
import com.github.fragivity.example.flow.ui.shop.ShopFragment
import com.github.fragivity.example.flow.ui.swipeback.FirstSwipeBackFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    OnFragmentOpenDrawerListener,
    OnLoginSuccessListener {

    private lateinit var binding: FlowActivityMainBinding

    private val mDrawer get() = binding.drawerLayout
    private val mNavigationView get() = binding.navView

    private var _mTvName: TextView? = null
    private val mTvName: TextView get() = _mTvName!!
    private var _mImageNav: ImageView? = null
    private val mImageNav: ImageView get() = _mImageNav!!

    private lateinit var navigator: FragivityNavHost

    override fun onCreate(savedInstanceState: Bundle?) {
        proxyFragmentFactory()
        super.onCreate(savedInstanceState)

        binding = FlowActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDefaultFragmentBackground()

        val navHostFragment = findNavHostFragment(R.id.nav_host)
        navHostFragment.loadRoot(HomeFragment::class)
        navHostFragment.showDebugView(this)
        navigator = navHostFragment.navigator
        initView()
    }

    private fun initView() {
        val toggle = ActionBarDrawerToggle(
            this, mDrawer,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        toggle.syncState()

        mNavigationView.setNavigationItemSelectedListener(this)
        mNavigationView.setCheckedItem(R.id.nav_home)

        val llNavHeader = mNavigationView.getHeaderView(0)
        llNavHeader.setOnClickListener {
            mDrawer.closeDrawer(GravityCompat.START)
            mDrawer.postDelayed({ goLogin() }, 250)
        }
        _mTvName = llNavHeader.findViewById(R.id.tv_name)
        _mImageNav = llNavHeader.findViewById(R.id.img_nav)

        supportFragmentManager.setFragmentResultListener(KEY_RESULT_OPEN_DRAW, this,
            FragmentResultListener { _, _ ->
                mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            })
    }

    override fun onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START)
            return
        }
        super.onBackPressed()
    }

    override fun onOpenDrawer() {
        if (!mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.openDrawer(GravityCompat.START)
            return
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        mDrawer.closeDrawer(GravityCompat.START)
        mDrawer.postDelayed({
            when (item.itemId) {
                R.id.nav_home -> {
                    navigator.push(HomeFragment::class) {
                        launchMode = LaunchMode.SINGLE_TASK
                        arguments = bundleOf("from" to "From:")
                    }
                }
                R.id.nav_discover -> {
                    navigator.push(DiscoverFragment::class) {
                        launchMode = LaunchMode.SINGLE_TASK
                    }
                }
                R.id.nav_shop -> {
                    navigator.push(ShopFragment::class) {
                        launchMode = LaunchMode.SINGLE_TASK
                    }
                }
                R.id.nav_login -> {
                    goLogin()
                }
                R.id.nav_swipe_back -> {
                    mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    navigator.push(FirstSwipeBackFragment::class) {
                        applyHorizontalInOut()
                        applyArguments(FirstSwipeBackFragment.ARGS_IS_FIRST to true)
                    }
                }
            }
        }, 300)
        return true
    }

    private fun goLogin() {
        navigator.push(LoginFragment::class) {
            applyVerticalInOut()
        }
    }

    override fun onLoginSuccess(account: String) {
        mTvName.text = account
        mImageNav.setImageResource(R.mipmap.ic_account_circle_white_48dp)
        showToast(R.string.sign_in_success)
    }

    companion object {
        const val KEY_RESULT_OPEN_DRAW = "main_key_result_open_draw"
    }
}