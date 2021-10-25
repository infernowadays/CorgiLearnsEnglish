package com.lazysecs.data.interactors

import android.app.Application
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lazysecs.data.db.SharedPrefs
import com.lazysecs.domain.models.Language


class LanguagesInteractor(
    private val application: Application,
    private val sharedPrefs: SharedPrefs,
) {

    fun loadLanguages(): List<Language> {
        val listType = object : TypeToken<List<Language>>() {}.type
        return Gson().fromJson(loadEnWordListFromAssets(), listType)
    }

    fun getFromLanguage(): Language {
        return loadLanguages().first { it.name == sharedPrefs.getFromLanguageName() }
    }

    fun getToLanguage(): Language {
        return loadLanguages().first { it.name == sharedPrefs.getToLanguageName() }
    }

    fun setFromLanguageName(name: String) {
        sharedPrefs.setFromLanguageName(name)
    }

    fun setToLanguageName(name: String) {
        sharedPrefs.setToLanguageName(name)
    }

    fun swapLanguages() {
        getFromLanguage().let { fromLanguage ->
            getToLanguage().let { toLanguage ->
                setFromLanguageName(toLanguage.name)
                setToLanguageName(fromLanguage.name)
            }
        }
    }

    private fun loadEnWordListFromAssets(): String {
        return application.assets.open("languages.json").bufferedReader().use { reader ->
            reader.readText()
        }
    }
}