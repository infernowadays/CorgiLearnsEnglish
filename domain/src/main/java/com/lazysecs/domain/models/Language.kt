package com.lazysecs.domain.models

import constants.LanguageCode

data class Language(
    var name: String,
    var code: String = LanguageCode.EN,
)