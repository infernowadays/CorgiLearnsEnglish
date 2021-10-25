package com.lazysecs.corgilearnsenglish.ui.base

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.lazysecs.corgilearnsenglish.R
import com.lazysecs.corgilearnsenglish.custom_views.LottieProgressView
import com.lazysecs.corgilearnsenglish.databinding.ToolbarBinding
import com.lazysecs.corgilearnsenglish.ui.MainActivity
import ru.terrakok.cicerone.Screen

abstract class BaseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setFragmentResultListener(Requests.ON_DISMISS_DIALOG) { _, _ ->
            Handler(Looper.getMainLooper()).postDelayed({
                getProgressBinding()?.resumeAnimation()
            }, 300)
        }
        return getRootView(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (getToolbarBinding() != null) {
            getToolbarBinding()!!.tvTitle.text = getToolbarTitle()
            getToolbarBinding()!!.root.setBackgroundColor(getToolbarColor())
            getToolbarBinding()!!.ivBack.setOnClickListener {
                back()
            }
        }
        setupView()
        setListeners()
        setStatusBarColor()
    }

    open fun showDialog(dialog: DialogFragment, tag: String) {
        getProgressBinding()?.pauseAnimation()
        dialog.show(requireActivity().supportFragmentManager, tag)
    }

    open fun getToolbarColor(): Int {
        return 0
    }

    open fun getToolbarTitle(): String? {
        return null
    }

    open fun getToolbarBinding(): ToolbarBinding? {
        return null
    }

    open fun setupView() {

    }

    open fun setListeners() {

    }

    open fun getProgressBinding(): LottieProgressView? {
        return null
    }

    open fun showSoftwareKeyboard(view: View) {
        (requireActivity() as MainActivity).showSoftwareKeyboard(view)
    }

    open fun hideSoftwareKeyboard(view: View) {
        (requireActivity() as MainActivity).hideSoftwareKeyboard(view)
    }

    protected fun setStatusBarColor(colorTo: Int = ContextCompat.getColor(requireActivity(), R.color.white)) {
        ValueAnimator.ofObject(ArgbEvaluator(), requireActivity().window.statusBarColor, colorTo).apply {
            addUpdateListener {
                requireActivity().window.statusBarColor = (it.animatedValue as Int)
            }
            start()
        }
    }

    protected abstract fun getRootView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View?

    protected fun navigateTo(screen: Screen?) {
        val baseActivity = activity as MainActivity?
        baseActivity?.router?.navigateTo(screen)
    }

    protected fun back() {
        val baseActivity = activity as MainActivity?
        baseActivity?.onBackPressed()
    }
}