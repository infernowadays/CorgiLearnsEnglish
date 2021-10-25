package com.lazysecs.corgilearnsenglish.ui.words

import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.lazysecs.corgilearnsenglish.databinding.WordItemBinding
import com.lazysecs.domain.models.Word

class WordVH(
    val binding: WordItemBinding,
    val clickCallback: (String, String) -> Unit,
    val swipeOpenedCallback: (String) -> Unit,
    val deleteCallback: (String) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(word: Word) {
        binding.tvWord.text = word.text
        binding.tvWordTranslation.text = word.translation
        binding.swipe.setSwipeListener(object : SwipeRevealLayout.SwipeListener {

            override fun onClosed(view: SwipeRevealLayout?) = Unit

            override fun onSlide(view: SwipeRevealLayout?, slideOffset: Float) = Unit

            override fun onOpened(view: SwipeRevealLayout?) {
                swipeOpenedCallback(word.text)
            }

        })
        binding.llRoot.setOnClickListener {
            clickCallback(word.text, word.languageCode)
        }
        binding.tvDelete.setOnClickListener {
            deleteCallback(word.text)
        }
    }
}