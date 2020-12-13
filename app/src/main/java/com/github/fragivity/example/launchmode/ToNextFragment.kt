package com.github.fragivity.example.launchmode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.fragivity.example.R
import kotlinx.android.synthetic.main.fragment_tonext.*
import kotlinx.android.synthetic.main.title_bar.*

class ToNextFragment(
    private val _title: String = "ToNextFragment",
    private val _next: ToNextFragment.() -> Unit = {}
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tonext, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        title_name.text = _title
        next.setOnClickListener { _next.invoke(this) }

    }
}