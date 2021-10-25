package com.lazysecs.corgilearnsenglish.ui.word_list

import com.lazysecs.domain.models.WordList

interface WordListClickListener {

    fun onWordListClick(wordList: WordList)

    fun onWordListLongClick(wordList: WordList)
}