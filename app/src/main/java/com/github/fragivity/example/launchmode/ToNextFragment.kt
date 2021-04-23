package com.github.fragivity.example.launchmode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.fragivity.example.R
import com.github.fragivity.example.databinding.FragmentTonextBinding
import com.github.fragivity.example.viewbinding.viewBinding

class ToNextFragment(
    private val _title: String = "ToNextFragment",
    private val _next: ToNextFragment.() -> Unit = {}
) : Fragment() {

    private val binding by viewBinding(FragmentTonextBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tonext, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.title_name).text = _title
        binding.next.setOnClickListener { _next.invoke(this) }
    }
}