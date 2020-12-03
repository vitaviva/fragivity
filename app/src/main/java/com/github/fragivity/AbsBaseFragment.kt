package com.github.fragivity

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.title_bar.*

/**
 * @author wangpeng.rocky@bytedance.com
 */
abstract class AbsBaseFragment(private val _supportBack: Boolean = true) : Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.e("wangp", "onActivityCreated:" + this.javaClass.simpleName)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("wangp", "onViewCreated:" + this.javaClass.simpleName)
        title_name?.text = titleName
        if (_supportBack == true) {
            title_back?.let {
                it.visibility = View.VISIBLE
                it.setOnClickListener {
                    pop()
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("wangp", "onCreate:" + this.javaClass.simpleName)
    }

    override fun onStart() {
        super.onStart()
        Log.e("wangp", "onStart:" + this.javaClass.simpleName)
    }

    override fun onResume() {
        super.onResume()
        Log.e("wangp", "onResume:" + this.javaClass.simpleName)
    }

    override fun onPause() {
        super.onPause()
        Log.e("wangp", "onPause:" + this.javaClass.simpleName)
    }

    override fun onStop() {
        super.onStop()
        Log.e("wangp", "onStop:" + this.javaClass.simpleName)
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.e("wangp", "onDestroy:" + this.javaClass.simpleName)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("wangp", "onDestroyView:" + this.javaClass.simpleName)
    }

    protected abstract val titleName: String?

}