package com.lazysecs.domain.models.microsoft.dictionary_lookup

data class DictionaryLookupResponse(
    var normalizedSource: String? = null,
    var displaySource: String? = null,
    var translations: List<DictionaryLookupTranslation>
)
