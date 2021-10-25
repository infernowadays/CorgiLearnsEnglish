package com.lazysecs.data.mappers

import com.lazysecs.data.db.entity.WordEntity
import com.lazysecs.domain.models.Word

fun WordEntity.toWord() = Word(
    text = text,
    languageCode = languageCode,
    wordListId = wordListId,
    translation = translation,
    createdMillis = createdMillis,
)

fun Word.toWordEntity() = WordEntity(
    text = text,
    languageCode = languageCode,
    wordListId = wordListId,
    translation = translation,
    createdMillis = createdMillis,
)