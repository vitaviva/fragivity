package com.github.fragivity.example.swipeback

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.fragivity.example.AbsBaseFragment
import com.github.fragivity.example.R
import com.github.fragivity.example.databinding.FragmentSwipeBackBinding
import com.github.fragivity.swipeback.setEnableGesture


class SwipeBackFragment : AbsBaseFragment() {

    private var _binding: FragmentSwipeBackBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSwipeBackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.swipeEnable.setOnCheckedChangeListener { _, checkedId ->
            setEnableGesture(checkedId == R.id.swipe_on)
        }
        setEnableGesture(binding.swipeEnable.checkedRadioButtonId == R.id.swipe_on)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override val titleName: String
        get() = "Swipe Back"
}
