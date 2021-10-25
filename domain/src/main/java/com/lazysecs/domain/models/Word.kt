package com.lazysecs.domain.models

data class Word(
    val text: String,
    val languageCode: String,
    val wordListId: String,
    val translation: String,
    val createdMillis: Long = System.currentTimeMillis(),
)