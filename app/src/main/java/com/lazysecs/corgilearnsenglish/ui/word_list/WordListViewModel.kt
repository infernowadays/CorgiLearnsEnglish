package com.lazysecs.corgilearnsenglish.ui.word_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazysecs.corgilearnsenglish.utils.TimeUtil
import com.lazysecs.data.db.entity.WordListEntity
import com.lazysecs.data.interactors.WordsInteractor
import com.lazysecs.data.interactors.WordsListInteractor
import com.lazysecs.data.mappers.toWordList
import com.lazysecs.domain.models.WordList
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class WordListViewModel(
    val wordsListInteractor: WordsListInteractor,
    val wordsInteractor: WordsInteractor,
) : ViewModel() {

    val wordListsLiveEvent = MutableLiveData<List<WordList>>()
    val createdWordListLiveEvent = MutableLiveData<WordList>()
    private var deletedWordListEntity: WordListEntity? = null

    fun getList() {
        viewModelScope.launch {
            wordsListInteractor.getAll().collect { wordsListEntities ->
                val wordLists = wordsListEntities.map { it.toWordList() }
                for (wordList in wordLists) {
                    wordList.wordsCount = wordsInteractor.getWordsCount(wordList.id)
                }
                wordListsLiveEvent.postValue(wordLists.sortedBy { -it.createdMillis })
            }
        }
    }

    fun createWordsList(name: String) {
        viewModelScope.launch {
            val uuid = generateUUID()
            wordsListInteractor.insert(
                WordListEntity(
                    id = uuid,
                    name = name,
                    createdMillis = TimeUtil.getCurrentMillis()
                )
            )
            createdWordListLiveEvent.postValue(wordsListInteractor.getById(uuid).toWordList())
        }
    }

    fun editWordListName(id: String, name: String) {
        viewModelScope.launch {
            wordsListInteractor.update(id, name)
        }
    }

    fun deleteWordList(wordListId: String) {
        viewModelScope.launch {
            deletedWordListEntity = wordsListInteractor.getById(wordListId)
            wordsListInteractor.delete(wordListId)
        }
    }

    private fun generateUUID(): String {
        return UUID.randomUUID().toString()
    }
}