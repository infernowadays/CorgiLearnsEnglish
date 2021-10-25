package com.lazysecs.corgilearnsenglish.ui.select_language

import androidx.lifecycle.ViewModel
import com.lazysecs.data.interactors.LanguagesInteractor
import com.lazysecs.domain.models.Language

class SelectLanguageViewModel(
    private val languagesInteractor: LanguagesInteractor,
) : ViewModel() {

    fun getLanguages(): List<Language> = languagesInteractor.loadLanguages()

    fun setFromLanguageName(name: String) {
        languagesInteractor.setFromLanguageName(name)
    }

    fun getFromLanguage(): String {
        return languagesInteractor.getFromLanguage().name
    }

    fun setToLanguageName(name: String) {
        languagesInteractor.setToLanguageName(name)
    }

    fun getToLanguage(): String {
        return languagesInteractor.getToLanguage().name
    }
}