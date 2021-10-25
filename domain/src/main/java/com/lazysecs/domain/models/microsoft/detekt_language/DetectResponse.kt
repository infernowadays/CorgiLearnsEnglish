package com.lazysecs.domain.models.microsoft.detekt_language

data class DetectResponse(
    val language: String,
    val isTranslationSupported: Boolean,
)