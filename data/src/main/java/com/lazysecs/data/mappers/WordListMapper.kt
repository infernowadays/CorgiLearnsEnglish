package com.lazysecs.data.mappers

import com.lazysecs.data.db.entity.WordListEntity
import com.lazysecs.domain.models.WordList

fun WordListEntity.toWordList() = WordList(id = id, name = name, createdMillis = createdMillis)

fun WordList.toWordListEntity() = WordListEntity(id = id, name = name, createdMillis = createdMillis)