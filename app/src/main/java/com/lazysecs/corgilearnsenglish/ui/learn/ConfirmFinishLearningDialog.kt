package com.lazysecs.corgilearnsenglish.ui.learn

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import com.lazysecs.corgilearnsenglish.databinding.DialogFragmentConfirmFinishLearningBinding
import com.lazysecs.corgilearnsenglish.ui.base.BaseDialog
import com.lazysecs.corgilearnsenglish.ui.base.Requests

class ConfirmFinishLearningDialog : BaseDialog() {

    private lateinit var binding: DialogFragmentConfirmFinishLearningBinding

    companion object {
        fun newInstance() = ConfirmFinishLearningDialog()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnFinish.setOnClickListener {
            setFragmentResult(
                Requests.CLOSE_DIALOG,
                Bundle()
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
        binding = DialogFragmentConfirmFinishLearningBinding.inflate(inflater, container, false)
        return binding.root
    }
}