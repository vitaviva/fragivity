package com.github.fragivity.example.backpress

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.github.fragivity.example.AbsBaseFragment
import com.github.fragivity.example.R
import com.github.fragivity.navigator
import com.github.fragivity.pop


class BackPressFragment : AbsBaseFragment() {

    private var extime: Long = 0
    private val cb by lazy {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (System.currentTimeMillis() - extime > 1000) {
                    Toast.makeText(context, "Click again to return", Toast.LENGTH_SHORT).show()
                    extime = System.currentTimeMillis()
                } else {
                    navigator.pop()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_back_press, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Handler().post {
            //make sure this will run before NavController.onBackPressed when Configurations changed
            requireActivity().onBackPressedDispatcher.addCallback(this, cb)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cb.remove() //not necessary
    }

    override val titleName: String?
        get() = "Back Press"

}
