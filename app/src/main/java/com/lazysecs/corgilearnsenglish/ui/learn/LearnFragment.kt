package com.lazysecs.corgilearnsenglish.ui.learn

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Insets
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import com.lazysecs.corgilearnsenglish.CorgiLearnsEnglishApp
import com.lazysecs.corgilearnsenglish.R
import com.lazysecs.corgilearnsenglish.databinding.FragmentLearnBinding
import com.lazysecs.corgilearnsenglish.databinding.ToolbarBinding
import com.lazysecs.corgilearnsenglish.ui.base.BaseFragment
import com.lazysecs.corgilearnsenglish.ui.base.Extras
import com.lazysecs.corgilearnsenglish.ui.base.InfoDialog
import com.lazysecs.corgilearnsenglish.ui.base.Requests
import javax.inject.Inject


class LearnFragment : BaseFragment(), NextWordCallback {

    private var _binding: FragmentLearnBinding? = null
    private val binding get() = _binding!!
    private var adapter: CardsAdapter? = null
    private var wordListId: String? = null
    internal var finishLearning: Boolean = false

    @Inject
    lateinit var viewModel: LearnViewModel

    companion object {
        const val ANIMATION_DURATION = 300L

        fun newInstance(wordListId: String): LearnFragment {
            val fragment = LearnFragment()
            val b = Bundle()
            b.putString(Extras.WORD_LIST_ID, wordListId)
            fragment.arguments = b
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (arguments != null) {
            wordListId = requireArguments().getString(Extras.WORD_LIST_ID)
        }
        setFragmentResultListener(Requests.CLOSE_DIALOG) { _, _ ->
            finishLearning()
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun injectDependencies() {
        CorgiLearnsEnglishApp.get(requireActivity())
            .appComponent
            .plus(LearnModule(this))
            .inject(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        adapter = CardsAdapter(this, context)
    }

    override fun getRootView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentLearnBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.learnProgress.isVisible = true
        wordListId?.let {
            viewModel.loadWords(it)
        }
        viewModel.wordsToLearnLiveData.observe(viewLifecycleOwner, { words ->
            for (word in words) {
                if (word.options.size != 5) {
                    val a = 4
                }
            }
            adapter?.submitList(words)
        })
    }

    override fun setupView() {
        binding.vpWords.adapter = adapter
        binding.vpWords.offscreenPageLimit = 10
        binding.vpWords.isUserInputEnabled = false
    }

    override fun next(lastCard: Boolean) {
        adapter?.let { animateProgress(binding.vpWords.currentItem, it.itemCount) }
        Handler(Looper.getMainLooper()).postDelayed({
            if (lastCard) {
                InfoDialog.newInstance(
                    title = getString(R.string.hooray),
                    buttonText = getString(R.string.finish),
                    lottieRaw = R.raw.celebration
                ).show(requireActivity().supportFragmentManager, Requests.INFO)
            } else {
                binding.vpWords.currentItem += 1
            }
        }, ANIMATION_DURATION)
    }

    override fun getToolbarTitle(): String {
        return "Learn words"
    }

    override fun getToolbarBinding(): ToolbarBinding {
        return binding.toolbar
    }

    internal fun showFinishLearningConfirmationDialog() {
        ConfirmFinishLearningDialog.newInstance()
            .show(requireActivity().supportFragmentManager, Requests.INTERRUPT_LEARNING)
    }


    private fun animateProgress(current: Int, total: Int) {
        ValueAnimator.ofInt(binding.toolbar.learnProgress.measuredWidth, getScreenWidth() * (current + 1) / total)
            .apply {
                addUpdateListener { valueAnimator ->
                    val animatedValue = valueAnimator.animatedValue as Int
                    val layoutParams: ViewGroup.LayoutParams = binding.toolbar.learnProgress.layoutParams
                    layoutParams.width = animatedValue
                    binding.toolbar.learnProgress.layoutParams = layoutParams
                }
                duration = ANIMATION_DURATION
                start()
            }
    }

    private fun getScreenWidth(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = requireActivity().windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.width() - insets.left - insets.right
        } else {
            val displayMetrics = DisplayMetrics()
            @Suppress("DEPRECATION") requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.widthPixels
        }
    }

    private fun finishLearning() {
        Handler(Looper.getMainLooper()).postDelayed({
            finishLearning = true
            back()
        }, 300)
    }
}