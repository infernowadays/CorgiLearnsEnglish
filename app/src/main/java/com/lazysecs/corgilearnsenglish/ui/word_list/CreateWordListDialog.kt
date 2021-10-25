package com.lazysecs.corgilearnsenglish.ui.word_list

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import androidx.room.util.StringUtil
import com.lazysecs.corgilearnsenglish.R
import com.lazysecs.corgilearnsenglish.databinding.DialogFragmentCreateWordListBinding
import com.lazysecs.corgilearnsenglish.ui.base.BaseDialog
import com.lazysecs.corgilearnsenglish.ui.base.Extras
import com.lazysecs.corgilearnsenglish.ui.base.Requests

class CreateWordListDialog : BaseDialog() {

    private lateinit var binding: DialogFragmentCreateWordListBinding
    private var wordListName: String? = null
    private var wordListId: String? = null

    companion object {
        fun newInstance(wordListId: String?, wordListName: String?) = CreateWordListDialog().apply {
            arguments = Bundle().apply {
                putString(Extras.WORD_LIST_ID, wordListId)
                putString(Extras.WORD_LIST_NAME, wordListName)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            wordListId = requireArguments().getString(Extras.WORD_LIST_ID)
            wordListName = requireArguments().getString(Extras.WORD_LIST_NAME)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (tag == Requests.CREATE_WORD_LIST) {
            binding.tvTitle.text = getString(R.string.create_words_list)
            binding.btnCreate.setText(R.string.create)
        } else if (tag == Requests.EDIT_WORD_LIST) {
            binding.tvTitle.text = getString(R.string.edit_word_list_name)
            binding.etName.setText(wordListName)
            binding.btnCreate.setText(R.string.update)
            binding.btnCreate.isEnabled = true
        }
        binding.btnCreate.setOnClickListener {
            binding.lottie.pauseAnimation()
            hideSoftwareKeyboard(binding.etName)
            setFragmentResult(
                tag ?: Requests.CREATE_WORD_LIST,
                Bundle().apply {
                    putString(
                        Extras.WORD_LIST_NAME, binding.etName.text.toString()
                            .trim().replace(" +".toRegex(), " ")
                    )
                    putString(Extras.WORD_LIST_ID, wordListId)
                }
            )
            dismiss()
        }
        binding.btnCancel.setOnClickListener {
            binding.lottie.pauseAnimation()
            hideSoftwareKeyboard(binding.etName)
            dismiss()
        }
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun afterTextChanged(s: Editable?) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.btnCreate.isEnabled = s.toString().isNotBlank()
            }
        })
        Handler(Looper.getMainLooper()).postDelayed({
            binding.lottie.playAnimation()
        }, 300)

    }

    override fun getRootView(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DialogFragmentCreateWordListBinding.inflate(inflater, container, false)
        return binding.root
    }
}