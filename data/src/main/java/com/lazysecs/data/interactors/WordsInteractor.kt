package com.lazysecs.data.interactors

import com.lazysecs.data.db.CorgiLearnsEnglishDatabase
import com.lazysecs.data.db.entity.WordEntity
import kotlinx.coroutines.flow.Flow

class WordsInteractor(
    private val database: CorgiLearnsEnglishDatabase
) {

    suspend fun insert(wordEntity: WordEntity) {
        database.wordDao().insert(wordEntity)
    }

    suspend fun wordInList(wordListId: String, text: String): Boolean =
        database.wordDao().wordInList(wordListId, text)

    suspend fun getWordsCount(wordListId: String): Int =
        database.wordDao().getWordsCount(wordListId)

    suspend fun removeFromList(text: String, wordListId: String) {
        database.wordDao().removeFromList(text, wordListId)
    }

    suspend fun getWordsByWordListId(wordListId: String): List<WordEntity> =
        database.wordDao().getSuspendByWordListId(wordListId)

    fun getFlowByWordListId(wordListId: String): Flow<List<WordEntity>> =
        database.wordDao().getFlowByWordListId(wordListId)
}
