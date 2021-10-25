package com.lazysecs.corgilearnsenglish.ui.create_account

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lazysecs.corgilearnsenglish.R
import com.lazysecs.corgilearnsenglish.databinding.FragmentCreateAccountBinding
import com.lazysecs.corgilearnsenglish.ui.base.BaseFragment
import java.util.regex.Matcher
import java.util.regex.Pattern

class CreateAccountFragment : BaseFragment() {

    private var _binding: FragmentCreateAccountBinding? = null
    private val binding get() = _binding!!


    companion object {
        private const val PASSWORD_MIN_LENGTH = 8

        fun newInstance(): CreateAccountFragment {
            val fragment = CreateAccountFragment()
            val b = Bundle()
            fragment.arguments = b
            return fragment
        }
    }

    override fun getRootView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentCreateAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setListeners() {
        binding.btnCreateAccount.setOnClickListener {

        }
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(s: Editable?) {
                setCreateAccountBtnEnabled()
            }
        })
        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isEmailValid(s.toString())) {
                    binding.etEmail.error = requireActivity().resources.getString(R.string.email_is_not_valid)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                setCreateAccountBtnEnabled()
            }
        })
        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length < PASSWORD_MIN_LENGTH) {
                    binding.etPassword.error =
                        requireActivity().resources.getString(R.string.password_length)
                } else if (binding.etConfirmPassword.text.isNotEmpty()
                    && binding.etPassword.text.toString() != binding.etConfirmPassword.text.toString()
                ) {
                    binding.etConfirmPassword.error =
                        requireActivity().resources.getString(R.string.passwords_must_match)
                }
                setCreateAccountBtnEnabled()
            }
        })
        binding.etConfirmPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != binding.etPassword.text.toString()) {
                    binding.etConfirmPassword.error =
                        requireActivity().resources.getString(R.string.passwords_must_match)
                }
                setCreateAccountBtnEnabled()
            }
        })
    }

    private fun isEmailValid(email: String): Boolean {
        val emailRegex = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,8}$"
        val pattern: Pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()
    }

    private fun setCreateAccountBtnEnabled() {
        binding.btnCreateAccount.isEnabled = binding.etName.text.isNotEmpty()
                && binding.etEmail.text.isNotEmpty()
                && binding.etPassword.text.isNotEmpty()
                && binding.etPassword.text.toString() == binding.etConfirmPassword.text.toString()
    }
}