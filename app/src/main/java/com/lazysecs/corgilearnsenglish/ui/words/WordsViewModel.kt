package com.lazysecs.corgilearnsenglish.ui.words

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazysecs.data.interactors.WordsInteractor
import com.lazysecs.data.mappers.toWord
import com.lazysecs.domain.models.Word
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class WordsViewModel(
    val wordsInteractor: WordsInteractor
) : ViewModel() {

    val wordsLiveEvent = MutableLiveData<List<Word>>()

    fun getWords(wordListId: String) {
        viewModelScope.launch {
            wordsInteractor.getFlowByWordListId(wordListId).collect { wordEntities ->
                wordsLiveEvent.postValue(wordEntities.map { it.toWord() }.sortedBy { -it.createdMillis })
            }
        }
    }

    fun removeWordFromList(text: String, wordListId: String) {
        viewModelScope.launch {
            wordsInteractor.removeFromList(text, wordListId)
        }
    }
}