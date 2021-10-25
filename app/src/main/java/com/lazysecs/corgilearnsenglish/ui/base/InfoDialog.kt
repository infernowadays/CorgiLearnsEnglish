package com.lazysecs.corgilearnsenglish.ui.base

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import com.lazysecs.corgilearnsenglish.databinding.DialogFragmentInfoBinding

class InfoDialog : BaseDialog() {

    private lateinit var binding: DialogFragmentInfoBinding
    private var title: String? = null
    private var buttonText: String? = null
    private var lottieRaw: Int = 0

    companion object {
        fun newInstance(title: String, buttonText: String, lottieRaw: Int) = InfoDialog().apply {
            arguments = Bundle().apply {
                putString(Extras.TITLE, title)
                putString(Extras.BUTTON_TEXT, buttonText)
                putInt(Extras.LOTTIE_RAW, lottieRaw)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            title = requireArguments().getString(Extras.TITLE)
            buttonText = requireArguments().getString(Extras.BUTTON_TEXT)
            lottieRaw = requireArguments().getInt(Extras.LOTTIE_RAW)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvTitle.text = title ?: ""
        binding.btnOk.text = buttonText ?: ""
        binding.lottie.setAnimation(lottieRaw)
        binding.btnOk.setOnClickListener {
            binding.lottie.pauseAnimation()
            setFragmentResult(
                Requests.CLOSE_DIALOG,
                Bundle()
            )
            dismiss()
        }
        Handler(Looper.getMainLooper()).postDelayed({
            binding.lottie.playAnimation()
        }, 300)
    }

    override fun getRootView(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DialogFragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }
}