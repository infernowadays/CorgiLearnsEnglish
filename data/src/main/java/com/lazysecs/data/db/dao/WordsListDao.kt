package com.lazysecs.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lazysecs.data.db.entity.WordListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WordsListDao {

    @Insert
    suspend fun insert(wordList: WordListEntity)

    @Query("UPDATE word_list SET name=:editedName WHERE id == :id")
    suspend fun update(editedName: String, id: String)

    @Query("DELETE FROM word_list WHERE id == :id")
    suspend fun delete(id: String)

    @Query("SELECT * FROM word_list WHERE id == :id")
    suspend fun get(id: String): WordListEntity

    @Query("SELECT * FROM word_list")
    fun getAll(): Flow<List<WordListEntity>>

    @Query("DELETE FROM word_list")
    suspend fun deleteAll()
}