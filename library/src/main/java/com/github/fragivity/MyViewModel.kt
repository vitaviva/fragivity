package com.github.fragivity

import androidx.collection.SparseArrayCompat
import androidx.collection.keyIterator
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigator

internal class MyViewModel : ViewModel() {
    val nodes = SparseArrayCompat<FragmentNavigator.Destination>()
}

internal fun FragmentActivity.saveToViewModel(destination: FragmentNavigator.Destination) {
    val vm = ViewModelProvider(this)[MyViewModel::class.java]
    if (vm.nodes.keyIterator().asSequence().any {
            it == destination.id
        }) return
    vm.nodes.put(destination.id, destination)
}


internal fun FragmentActivity.removeFromViewModel(id: Int) {
    val vm = ViewModelProvider(this)[MyViewModel::class.java]
    vm.nodes.remove(id)
}