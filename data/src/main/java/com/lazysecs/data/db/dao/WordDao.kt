package com.lazysecs.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lazysecs.data.db.entity.WordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {

    @Insert
    suspend fun insert(wordEntity: WordEntity)

    @Query("SELECT EXISTS(SELECT * FROM word WHERE wordListId == :wordListId AND text = :text)")
    suspend fun wordInList(wordListId: String, text: String): Boolean

    @Query("SELECT COUNT(*) FROM word WHERE wordListId == :wordListId")
    suspend fun getWordsCount(wordListId: String): Int

    @Query("DELETE FROM word")
    suspend fun deleteAll()

    @Query("DELETE FROM word WHERE text == :text AND wordListId == :wordListId")
    suspend fun removeFromList(text: String, wordListId: String)

    @Query("SELECT * FROM word WHERE wordListId == :wordListId")
    suspend fun getSuspendByWordListId(wordListId: String): List<WordEntity>

    @Query("SELECT * FROM word WHERE wordListId == :wordListId")
    fun getFlowByWordListId(wordListId: String): Flow<List<WordEntity>>
}