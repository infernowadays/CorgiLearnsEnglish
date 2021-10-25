package com.lazysecs.domain.models.microsoft.translate

data class TranslateResponse(
    var detectedLanguage: DetectedLanguage? = null,
    var translations: List<Translation>,
)