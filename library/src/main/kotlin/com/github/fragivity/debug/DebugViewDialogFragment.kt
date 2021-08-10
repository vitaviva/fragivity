package com.github.fragivity.debug

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

class DebugViewDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val debugViewFragmentName = javaClass.simpleName
        val fragmentRecords = requireActivity().getFragmentRecords {
            debugViewFragmentName != it.javaClass.simpleName
        }

        val container = DebugHierarchyViewContainer(requireContext()).apply {
            bindFragmentRecords(fragmentRecords)
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        return AlertDialog.Builder(requireContext())
            .setView(container)
            .setPositiveButton(android.R.string.cancel, null)
            .setCancelable(true)
            .create()
    }

    override fun show(manager: FragmentManager, tag: String?) {
        val oldDialog = manager.findFragmentByTag(tag)
        if (oldDialog == null) {
            super.show(manager, tag)
        } else {
            val ft = manager.beginTransaction()
            ft.remove(oldDialog)
            super.show(ft, tag)
        }
    }
}