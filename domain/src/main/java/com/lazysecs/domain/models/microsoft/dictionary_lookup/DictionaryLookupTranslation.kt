package com.lazysecs.domain.models.microsoft.dictionary_lookup

data class DictionaryLookupTranslation(
    var displayTarget: String,
    var addedToWordList: Boolean = false,
    var normalizedTarget: String? = null,
    var backTranslations: List<BackTranslation> = emptyList(),
)