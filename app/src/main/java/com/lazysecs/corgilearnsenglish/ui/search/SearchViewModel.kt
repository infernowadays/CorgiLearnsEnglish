package com.lazysecs.corgilearnsenglish.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazysecs.data.interactors.LanguagesInteractor
import com.lazysecs.data.interactors.SearchInteractor
import com.lazysecs.data.interactors.WordsInteractor
import com.lazysecs.data.mappers.toWordEntity
import com.lazysecs.domain.models.Result
import com.lazysecs.domain.models.Word
import com.lazysecs.domain.models.microsoft.dictionary_lookup.DictionaryLookupTranslation
import com.lazysecs.domain.models.microsoft.translate.Translation
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.launch
import java.util.*

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val wordsInteractor: WordsInteractor,
    private val languagesInteractor: LanguagesInteractor,
) : ViewModel() {

    val showAddWordToListButtonLiveEvent = MutableLiveData<Boolean>()
    private var word: Word? = null

    fun getWord(): Word? {
        return word
    }

    fun addWordToList(word: Word) {
        viewModelScope.launch {
            wordsInteractor.insert(word.toWordEntity())
        }
    }

    fun translate(text: String, wordListId: String): Observable<Translation> {
        val fromLanguage = languagesInteractor.getFromLanguage()
        val toLanguage = languagesInteractor.getToLanguage()
        return searchInteractor.translate(
            text = text,
            from = fromLanguage.code,
            to = toLanguage.code,
        )
            .flatMapObservable { result ->
                when (result) {
                    is Result.Success -> {
                        if (result.data.isNullOrEmpty()) {
                            Observable.empty()
                        } else {
                            wordInList(text, wordListId)
                            val translation = result.data[0]
                            word = Word(
                                text = text.toLowerCase(Locale.getDefault()),
                                languageCode = fromLanguage.code,
                                translation = translation.text,
                                wordListId = wordListId,
                            )
                            // translate 1 word, so in this case 1 value from list needed cause request returns list
                            Observable.just(translation)
                        }
                    }
                    is Result.Error -> {
                        Observable.empty()
                    }
                }
            }
    }

    fun dictionaryLookup(text: String): Observable<List<DictionaryLookupTranslation>> {
        return searchInteractor.dictionaryLookup(
            text = text,
            from = languagesInteractor.getFromLanguage().code,
            to = languagesInteractor.getToLanguage().code,
        ).flatMapObservable { result ->
            when (result) {
                is Result.Success -> {
                    if (result.data.isEmpty() || result.data[0].translations.isNullOrEmpty()) {
                        Observable.just(emptyList())
                    } else {
                        Observable.just(result.data[0].translations)
                    }
                }
                is Result.Error -> {
                    Observable.just(emptyList())
                }
            }
        }
    }

    private fun wordInList(text: String, wordListId: String) {
        viewModelScope.launch {
            val wordInList = wordsInteractor.wordInList(wordListId, text)
            showAddWordToListButtonLiveEvent.value = !wordInList
        }
    }

    fun getLanguages(): Pair<String, String> {
        return languagesInteractor.getFromLanguage().name to languagesInteractor.getToLanguage().name
    }

    fun swapLanguages() {
        languagesInteractor.swapLanguages()
    }

    fun getFromLanguageCode(): String {
        return languagesInteractor.getFromLanguage().code
    }
}