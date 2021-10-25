package com.lazysecs.corgilearnsenglish.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
fun <VM : ViewModel> Fragment.createViewModel(factory: () -> VM): VM {
    val factoryViewModel = factory()
    return ViewModelProvider(this, object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return factoryViewModel as T
        }
    }).get(factoryViewModel::class.java)
}
