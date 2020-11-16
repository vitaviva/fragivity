package com.github.fragivity

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment

/**
 * @author wangpeng.rocky@bytedance.com
 */
abstract class AbsBaseFragment : Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.e("wangp", "onActivityCreated:" + this.javaClass.simpleName)
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

}