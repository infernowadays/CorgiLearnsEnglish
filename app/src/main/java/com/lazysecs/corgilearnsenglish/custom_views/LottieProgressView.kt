package com.lazysecs.corgilearnsenglish.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.lazysecs.corgilearnsenglish.databinding.LottieProgressViewBinding

class LottieProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : LinearLayout(context, attrs, defStyle) {

    private var binding = LottieProgressViewBinding.inflate(LayoutInflater.from(context), this, true)

    fun hide() {
        isVisible = false
    }

    fun show(message: String, lottieRaw: Int) {
        binding.tvMessage.text = message
        binding.lottie.setAnimation(lottieRaw)
        binding.lottie.playAnimation()
        isVisible = true
    }

    fun pauseAnimation() {
        if (isVisible) binding.lottie.pauseAnimation()
    }

    fun resumeAnimation() {
        if (isVisible) binding.lottie.resumeAnimation()
    }
}