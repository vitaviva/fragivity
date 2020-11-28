package com.github.fragivity.backpress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.github.fragivity.AbsBaseFragment
import com.github.fragivity.pop
import com.my.example.R


class BackPressFragment : AbsBaseFragment() {

    private var extime: Long = 0
    private val cb by lazy {
        object:OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (System.currentTimeMillis() - extime > 1000) {
                    Toast.makeText(context, "Click again to return", Toast.LENGTH_SHORT).show()
                    extime = System.currentTimeMillis()
                } else {
                    pop()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragivity_back_press, container, false)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, cb)
    }

    override fun onDestroy() {
        super.onDestroy()
        cb.remove() //not necessary
    }

}
