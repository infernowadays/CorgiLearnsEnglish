package com.lazysecs.data.mappers

import com.lazysecs.data.db.entity.WordEntity
import com.lazysecs.domain.models.microsoft.dictionary_lookup.DictionaryLookupTranslation

fun WordEntity.toTranslation() = DictionaryLookupTranslation(displayTarget = text)