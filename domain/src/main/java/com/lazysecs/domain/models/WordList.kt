package com.lazysecs.domain.models

data class WordList(
    val id: String,
    val name: String,
    val createdMillis: Long = System.currentTimeMillis(),
    var wordsCount: Int = 0,
)