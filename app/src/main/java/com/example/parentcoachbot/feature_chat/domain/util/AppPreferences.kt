package com.example.parentcoachbot.feature_chat.domain.util

import android.content.Context
import android.content.SharedPreferences

class AppPreferences(context: Context) {
    private val sharedPrefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "MyAppPreferences"
        private const val DEFAULT_LANGUAGE_KEY = "default_language"
        private const val IS_DB_INIT_KEY = "is_db_intitialized"
    }

    fun getDefaultLanguage(): String {
        return sharedPrefs.getString(DEFAULT_LANGUAGE_KEY, Language.English.isoCode) ?: Language.English.isoCode
    }

    fun getIsDbInitialized(): Boolean {
        return sharedPrefs.getBoolean(IS_DB_INIT_KEY, false)
    }

    fun setIsDbInitialized(isInit: Boolean)  {
        return sharedPrefs.edit().putBoolean(IS_DB_INIT_KEY, isInit).apply()
    }

    fun setDefaultLanguage(language: String) {
        sharedPrefs.edit().putString(DEFAULT_LANGUAGE_KEY, language).apply()
    }
}