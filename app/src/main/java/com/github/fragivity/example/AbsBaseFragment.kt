package com.github.fragivity.example

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.fragivity.navigator
import com.github.fragivity.pop

abstract class AbsBaseFragment(private val _supportBack: Boolean = true) : Fragment() {

    companion object {
        const val TAG = "Fragivity"
    }

    @Suppress("DEPRECATION")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.e(TAG, "onActivityCreated:${this.javaClass.simpleName}(${this.hashCode().toString(16)})")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e(TAG, "onViewCreated:${this.javaClass.simpleName}(${this.hashCode().toString(16)})")
        view.findViewById<TextView>(R.id.title_name)?.text = titleName
        if (_supportBack) {
            view.findViewById<TextView>(R.id.title_back)?.let {
                it.visibility = View.VISIBLE
                it.setOnClickListener {
                    navigator.pop()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "onCreate:${this.javaClass.simpleName}(${this.hashCode().toString(16)})")
    }

    override fun onStart() {
        super.onStart()
        Log.e(TAG, "onStart:${this.javaClass.simpleName}(${this.hashCode().toString(16)})")
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume:${this.javaClass.simpleName}(${this.hashCode().toString(16)})")
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG, "onPause:${this.javaClass.simpleName}(${this.hashCode().toString(16)})")
    }

    override fun onStop() {
        super.onStop()
        Log.e(TAG, "onStop:${this.javaClass.simpleName}(${this.hashCode().toString(16)})")
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy:${this.javaClass.simpleName}(${this.hashCode().toString(16)})")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e(TAG, "onDestroyView:${this.javaClass.simpleName}(${this.hashCode().toString(16)})")
    }

    protected abstract val titleName: String?

}