package com.lazysecs.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lazysecs.data.db.dao.WordDao
import com.lazysecs.data.db.dao.WordsListDao
import com.lazysecs.data.db.entity.WordEntity
import com.lazysecs.data.db.entity.WordListEntity

@Database(
    entities = [
        WordListEntity::class,
        WordEntity::class
    ], version = 1, exportSchema = false
)
abstract class CorgiLearnsEnglishDatabase : RoomDatabase() {

    abstract fun wordsListDao(): WordsListDao

    abstract fun wordDao(): WordDao
}