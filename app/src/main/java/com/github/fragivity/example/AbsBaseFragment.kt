package com.github.fragivity.example

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.github.fragivity.navigator
import com.github.fragivity.pop
import kotlinx.android.synthetic.main.title_bar.*

abstract class AbsBaseFragment(private val _supportBack: Boolean = true) : Fragment() {

    companion object {
        const val TAG = "Fragivity"
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.e(TAG, "onActivityCreated:" + this.javaClass.simpleName)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e(TAG, "onViewCreated:" + this.javaClass.simpleName)
        title_name?.text = titleName
        if (_supportBack == true) {
            title_back?.let {
                it.visibility = View.VISIBLE
                it.setOnClickListener {
                    navigator.pop()
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "onCreate:" + this.javaClass.simpleName)
    }

    override fun onStart() {
        super.onStart()
        Log.e(TAG, "onStart:" + this.javaClass.simpleName)
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume:" + this.javaClass.simpleName)
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG, "onPause:" + this.javaClass.simpleName)
    }

    override fun onStop() {
        super.onStop()
        Log.e(TAG, "onStop:" + this.javaClass.simpleName)
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy:" + this.javaClass.simpleName)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e(TAG, "onDestroyView:" + this.javaClass.simpleName)
    }

    protected abstract val titleName: String?

}