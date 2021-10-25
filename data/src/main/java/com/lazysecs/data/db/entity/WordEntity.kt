package com.lazysecs.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word")
data class WordEntity(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "languageCode") val languageCode: String,
    @ColumnInfo(name = "wordListId") val wordListId: String,
    @ColumnInfo(name = "translation") val translation: String,
    @ColumnInfo(name = "createdMillis") val createdMillis: Long = System.currentTimeMillis(),
)

