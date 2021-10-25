package com.lazysecs.data.interactors

import com.lazysecs.data.db.CorgiLearnsEnglishDatabase
import com.lazysecs.data.db.entity.WordListEntity
import kotlinx.coroutines.flow.Flow

class WordsListInteractor(
    private val database: CorgiLearnsEnglishDatabase
) {

    suspend fun insert(wordListEntity: WordListEntity) {
        database.wordsListDao().insert(wordListEntity)
    }

    suspend fun update(wordListId: String, editedName: String) {
        database.wordsListDao().update(editedName, wordListId)
    }

    suspend fun delete(wordListId: String) {
        database.wordsListDao().delete(wordListId)
    }

    fun getAll(): Flow<List<WordListEntity>> = database.wordsListDao().getAll()

    suspend fun getById(id: String): WordListEntity {
        return database.wordsListDao().get(id)
    }
}
