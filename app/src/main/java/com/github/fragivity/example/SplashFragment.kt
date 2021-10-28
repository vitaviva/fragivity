package com.github.fragivity.example

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.fragivity.applySlideInOut
import com.github.fragivity.navigator
import com.github.fragivity.pushTo
import kotlinx.coroutines.delay

class SplashFragment : Fragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return TextView(inflater.context).apply {
            gravity = Gravity.CENTER
            textSize = 20f
            text = "Welcome"
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenResumed {
            delay(500)
            navigator.pushTo(HomeFragment::class) {
                popSelf = true
                applySlideInOut()
            }
        }
    }

}