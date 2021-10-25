package com.lazysecs.corgilearnsenglish.ui.login

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lazysecs.corgilearnsenglish.databinding.FragmentLoginBinding
import com.lazysecs.corgilearnsenglish.ui.base.BaseFragment

class LoginFragment : BaseFragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun getRootView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }
}