package com.lazysecs.data.db

import android.content.Context
import android.content.SharedPreferences

class SharedPrefs(context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFERENCE_NAME = "com.lazysecs.corgilearnsenglish"
        private const val ACCOUNT_TOKEN = "ACCOUNT_TOKEN"
        private const val FROM_LANGUAGE_NAME = "FROM_LANGUAGE_NAME"
        private const val TO_LANGUAGE_NAME = "TO_LANGUAGE_NAME"
    }

    fun setAccountToken(accountToken: String) {
        preferences.edit().putString(ACCOUNT_TOKEN, accountToken).apply()
    }

    fun getAccountToken(): String? {
        return preferences.getString(ACCOUNT_TOKEN, null)
    }

    fun setFromLanguageName(name: String) {
        preferences.edit().putString(FROM_LANGUAGE_NAME, name).apply()
    }

    fun getFromLanguageName(): String {
        return preferences.getString(FROM_LANGUAGE_NAME, "English").toString()
    }

    fun setToLanguageName(name: String) {
        preferences.edit().putString(TO_LANGUAGE_NAME, name).apply()
    }

    fun getToLanguageName(): String {
        return preferences.getString(TO_LANGUAGE_NAME, "Russian").toString()
    }
}