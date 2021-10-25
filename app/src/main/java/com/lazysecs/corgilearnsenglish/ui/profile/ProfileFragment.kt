package com.lazysecs.corgilearnsenglish.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lazysecs.corgilearnsenglish.databinding.FragmentProfileBinding
import com.lazysecs.corgilearnsenglish.ui.base.BaseFragment

class ProfileFragment : BaseFragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun getRootView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
}