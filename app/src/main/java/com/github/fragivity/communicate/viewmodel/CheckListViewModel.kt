package com.github.fragivity.communicate.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.fragivity.communicate.Item
import com.github.fragivity.communicate.originList

class ListViewModel : ViewModel() {
    val liveData = MutableLiveData(originList)

    fun updateCheckState(id: Int, checked: Boolean) {
        val new = liveData.value!!.mapIndexed { index: Int, item: Item ->
            if (index == id) {
                item.copy(checked = checked)
            } else item
        }
        liveData.value = new
    }
}
