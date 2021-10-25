package com.lazysecs.corgilearnsenglish.ui.word_list

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import com.lazysecs.corgilearnsenglish.databinding.DialogFragmentDeleteWordListBinding
import com.lazysecs.corgilearnsenglish.ui.base.BaseDialog
import com.lazysecs.corgilearnsenglish.ui.base.Extras
import com.lazysecs.corgilearnsenglish.ui.base.Requests

class DeleteWordListDialog : BaseDialog() {

    private lateinit var binding: DialogFragmentDeleteWordListBinding
    private var wordListId: String? = null

    companion object {
        fun newInstance(wordListId: String) = DeleteWordListDialog().apply {
            arguments = Bundle().apply {
                putString(Extras.WORD_LIST_ID, wordListId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            wordListId = requireArguments().getString(Extras.WORD_LIST_ID)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnDelete.setOnClickListener {
            setFragmentResult(
                Requests.DELETE_WORD_LIST,
                Bundle().apply {
                    putString(Extras.WORD_LIST_ID, wordListId)
                }
            )
            binding.lottie.pauseAnimation()
            dismiss()
        }
        binding.btnCancel.setOnClickListener {
            binding.lottie.pauseAnimation()
            dismiss()
        }
        Handler(Looper.getMainLooper()).postDelayed({
            binding.lottie.playAnimation()
        }, 300)

    }

    override fun getRootView(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DialogFragmentDeleteWordListBinding.inflate(inflater, container, false)
        return binding.root
    }
}