package com.lazysecs.corgilearnsenglish.ui.word_list

import android.content.res.Resources
import androidx.recyclerview.widget.RecyclerView
import com.lazysecs.corgilearnsenglish.R
import com.lazysecs.corgilearnsenglish.databinding.WordListItemBinding
import com.lazysecs.corgilearnsenglish.utils.ColorUtil
import com.lazysecs.domain.models.WordList

class WordListVH(
    private val binding: WordListItemBinding,
    private val wordListClickListener: WordListClickListener,
    private val resources: Resources,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(wordList: WordList) {
        binding.tvName.text = wordList.name
        binding.tvWordsCount.text =
            resources.getQuantityString(R.plurals.words_count, wordList.wordsCount, wordList.wordsCount)
        binding.llRoot.setBackgroundColor(ColorUtil.getColor((wordList.createdMillis / wordList.name.length).toInt()))
        binding.llRoot.setOnClickListener {
            wordListClickListener.onWordListClick(wordList)
        }
        binding.llRoot.setOnLongClickListener {
            wordListClickListener.onWordListLongClick(wordList)
            true
        }
    }
}