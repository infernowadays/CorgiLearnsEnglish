package com.lazysecs.corgilearnsenglish.ui.words

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.recyclerview.widget.RecyclerView
import com.lazysecs.corgilearnsenglish.CorgiLearnsEnglishApp
import com.lazysecs.corgilearnsenglish.R
import com.lazysecs.corgilearnsenglish.custom_views.LottieProgressView
import com.lazysecs.corgilearnsenglish.databinding.FragmentWordsBinding
import com.lazysecs.corgilearnsenglish.databinding.ToolbarBinding
import com.lazysecs.corgilearnsenglish.navigation.Screens
import com.lazysecs.corgilearnsenglish.ui.base.BaseFragment
import com.lazysecs.corgilearnsenglish.ui.base.Extras
import com.lazysecs.corgilearnsenglish.ui.base.InfoDialog
import com.lazysecs.corgilearnsenglish.ui.base.Requests
import com.lazysecs.corgilearnsenglish.utils.ColorUtil
import javax.inject.Inject


class WordsFragment : BaseFragment() {

    private var _binding: FragmentWordsBinding? = null
    private val binding get() = _binding!!
    private val adapter: WordsAdapter = WordsAdapter(
        { viewModel.removeWordFromList(it, wordListId) },
        { text, languageCode -> navigateTo(Screens.SearchWords(wordListId, text, languageCode)) }
    )
    private var animationState: AnimationState = AnimationState.HideAnimation
    private lateinit var wordListId: String
    private lateinit var wordListName: String
    private var wordListCreatedMillis: Long = 0

    @Inject
    lateinit var viewModel: WordsViewModel

    companion object {
        const val ANIMATION_DURATION = 300L

        fun newInstance(wordListId: String, wordListName: String, createdMillis: Long): WordsFragment {
            val fragment = WordsFragment()
            val b = Bundle()
            b.putString(Extras.WORD_LIST_ID, wordListId)
            b.putString(Extras.WORD_LIST_NAME, wordListName)
            b.putLong(Extras.WORD_LIST_CREATED_MILLIS, createdMillis)
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
            wordListId = requireArguments().getString(Extras.WORD_LIST_ID) ?: ""
            wordListName = requireArguments().getString(Extras.WORD_LIST_NAME) ?: ""
            wordListCreatedMillis = requireArguments().getLong(Extras.WORD_LIST_CREATED_MILLIS)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor(ColorUtil.getColor((wordListCreatedMillis / wordListName.length).toInt()))
        binding.toolbar.tvTitle.isSelected = true
        binding.rvWords.adapter = adapter
        viewModel.getWords(wordListId)
        viewModel.wordsLiveEvent.observe(viewLifecycleOwner, {
            adapter.submitList(it)
            if (it.isNullOrEmpty()) {
                binding.progress.show(getString(R.string.press_plus_to_add_words), R.raw.nothing_found)
            } else {
                binding.progress.hide()
            }
        })
    }

    override fun getRootView(inflater: LayoutInflater, container: ViewGroup?): View {
        _binding = FragmentWordsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun getToolbarColor(): Int {
        return ColorUtil.getColor((wordListCreatedMillis / wordListName.length).toInt())
    }

    override fun getToolbarTitle(): String {
        return wordListName
    }

    override fun getToolbarBinding(): ToolbarBinding {
        return binding.toolbar
    }

    private fun injectDependencies() {
        CorgiLearnsEnglishApp.get(requireActivity())
            .appComponent
            .plus(WordsModule(this))
            .inject(this)
    }

    override fun getProgressBinding(): LottieProgressView {
        return binding.progress
    }

    override fun setListeners() {
        binding.btnLearn.setOnClickListener {
            if (adapter.itemCount < 10) {
                showDialog(
                    InfoDialog.newInstance(
                        title = getString(R.string.minimum_words_to_start_learning),
                        buttonText = getString(R.string.ok),
                        lottieRaw = R.raw.lick
                    ), Requests.INFO
                )
            } else {
                navigateTo(Screens.Learn(wordListId))
            }
        }
        binding.ivAddWord.setOnClickListener {
            navigateTo(Screens.SearchWords(wordListId))
        }
        binding.rvWords.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (animationState == AnimationState.NoAnimation) return
                if (dy > 0 && animationState == AnimationState.HideAnimation) {
                    hideFloatingButtons()
                } else if (dy < 0 && animationState == AnimationState.ShowAnimation) {
                    showFloatingButtons()
                }
            }
        })
    }

    private fun hideFloatingButtons() {
        val animation = TranslateAnimation(
            0f,
            0f,
            0f,
            binding.llFloatingButtons.height.toFloat()
        )
        animation.duration = ANIMATION_DURATION
        animation.fillAfter = true
        animation.setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationRepeat(animation: Animation?) = Unit

            override fun onAnimationStart(animation: Animation?) {
                makeClickable(binding.llFloatingButtons, false)
                animationState = AnimationState.NoAnimation
            }

            override fun onAnimationEnd(animation: Animation?) {
                animationState = AnimationState.ShowAnimation
            }
        })
        binding.llFloatingButtons.startAnimation(animation)
    }

    private fun showFloatingButtons() {
        val animation = TranslateAnimation(
            0f,
            0f,
            binding.llFloatingButtons.height.toFloat(),
            0f
        )
        animation.duration = ANIMATION_DURATION
        animation.fillAfter = true
        animation.setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationRepeat(animation: Animation?) = Unit

            override fun onAnimationStart(animation: Animation?) {
                animationState = AnimationState.NoAnimation
            }

            override fun onAnimationEnd(animation: Animation?) {
                makeClickable(binding.llFloatingButtons, true)
                animationState = AnimationState.HideAnimation
            }
        })
        binding.llFloatingButtons.startAnimation(animation)
    }

    private fun makeClickable(view: View, clickable: Boolean) {
        view.isEnabled = clickable
        if (view is ViewGroup) {
            for (idx in 0 until view.childCount) {
                makeClickable(view.getChildAt(idx), clickable)
            }
        }
    }

    sealed class AnimationState {
        object ShowAnimation : AnimationState()
        object HideAnimation : AnimationState()
        object NoAnimation : AnimationState()
    }
}