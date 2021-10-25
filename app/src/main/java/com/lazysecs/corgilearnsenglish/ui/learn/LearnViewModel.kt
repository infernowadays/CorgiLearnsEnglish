package com.lazysecs.corgilearnsenglish.ui.learn

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.lazysecs.data.interactors.LanguagesInteractor
import com.lazysecs.data.interactors.WordsInteractor
import com.lazysecs.data.mappers.toWord
import com.lazysecs.domain.models.Word
import kotlinx.coroutines.launch
import java.util.*

class LearnViewModel(
    private val application: Application,
    private val wordsInteractor: WordsInteractor,
    private val languagesInteractor: LanguagesInteractor,
) : ViewModel() {

    val wordsToLearnLiveData = MutableLiveData<List<WordCard>>()

    companion object {
        private const val WORDS_FILE_PATH = "_word_list.json"
        private const val ANSWER_OPTIONS_COUNT = 4
    }

    fun loadWords(wordListId: String) {
        viewModelScope.launch {
            val words = wordsInteractor.getWordsByWordListId(wordListId)
                .map { it.toWord() }
                .shuffled()
                .take(10)

            val firstLanguageCode = languagesInteractor.getFromLanguage().code
            val secondLanguageCode = languagesInteractor.getToLanguage().code

            val firstLanguageOptionWords = loadAndShuffleWordListFromAssets(
                firstLanguageCode.toLowerCase(Locale.getDefault()) + WORDS_FILE_PATH
            ).subList(
                0,
                words.count { it.languageCode == firstLanguageCode } * ANSWER_OPTIONS_COUNT
            )
            val secondLanguageOptionWords = loadAndShuffleWordListFromAssets(
                secondLanguageCode.toLowerCase(Locale.getDefault()) + WORDS_FILE_PATH
            ).subList(
                0,
                words.count { it.languageCode == secondLanguageCode } * ANSWER_OPTIONS_COUNT
            )

            if (!firstLanguageOptionWords.isNullOrEmpty() || !secondLanguageOptionWords.isNullOrEmpty()) {
                mutableListOf<WordCard>().apply {
                    var firstIndex = 0
                    var secondIndex = 0
                    words.forEach { word ->
                        if (word.languageCode.equals(firstLanguageCode, true)) {
                            add(createWordCard(word, firstLanguageOptionWords, firstIndex))
                            firstIndex++
                        } else if (word.languageCode.equals(secondLanguageCode, true)) {
                            add(createWordCard(word, secondLanguageOptionWords, secondIndex))
                            secondIndex++
                        }
                    }
                    shuffle()
                    wordsToLearnLiveData.postValue(this)
                }
            }
        }
    }

    private fun createWordCard(word: Word, options: MutableList<String>, index: Int): WordCard {
        val subList = options.subList(
            index * ANSWER_OPTIONS_COUNT,
            (index + 1) * ANSWER_OPTIONS_COUNT
        ).toMutableList()
        subList.add(word.text)
        return WordCard(
            word = word.translation,
            answer = word.text,
            options = subList.shuffled(),
        )
    }

    private fun loadAndShuffleWordListFromAssets(fileName: String): MutableList<String> {
        val json = application.assets.open(fileName).bufferedReader().use { it.readText() }
        return Gson().fromJson(json, WordList::class.java).words.shuffled().toMutableList()
    }

    data class WordList(@SerializedName("words") val words: List<String>)
}