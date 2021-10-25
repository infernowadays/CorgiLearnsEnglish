package com.lazysecs.corgilearnsenglish.ui.base

import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.lazysecs.corgilearnsenglish.R
import com.lazysecs.corgilearnsenglish.ui.MainActivity

abstract class BaseDialog : DialogFragment() {

    companion object {
        private const val DIM_AMOUNT = 0.75f
        private const val DIM_EMPTY = 0f
    }

    override fun getTheme() = R.style.ModalDialogTheme

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCancelable(false)
        return getRootView(inflater = inflater, container = container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView = view
        launchInAnimation()
    }

    override fun dismiss() {
        setFragmentResult(
            Requests.ON_DISMISS_DIALOG,
            Bundle()
        )
        launchOutAnimation()
    }

    open fun hideSoftwareKeyboard(view: View) {
        (requireActivity() as MainActivity).hideSoftwareKeyboard(view)
    }

    abstract fun getRootView(inflater: LayoutInflater, container: ViewGroup?): View

    private fun launchInAnimation() {
        val slideInAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_dialog)
        ValueAnimator.ofFloat(DIM_EMPTY, DIM_AMOUNT).apply {
            duration = slideInAnimation.duration
            addUpdateListener { dialog?.window?.setDimAmount(it.animatedValue as Float) }
            start()
        }
        rootView.startAnimation(slideInAnimation)
    }

    private fun launchOutAnimation() {
        val slideOutAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_dialog)
        ValueAnimator.ofFloat(DIM_AMOUNT, DIM_EMPTY).apply {
            duration = slideOutAnimation.duration
            addUpdateListener { dialog?.window?.setDimAmount(it.animatedValue as Float) }
            start()
        }

        slideOutAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                parentDismiss()
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })

        rootView.startAnimation(slideOutAnimation)
    }

    private fun parentDismiss() {
        super.dismiss()
    }
}