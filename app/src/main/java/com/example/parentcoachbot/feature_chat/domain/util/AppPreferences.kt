package com.example.parentcoachbot.feature_chat.domain.util

import android.content.Context
import android.content.SharedPreferences

class AppPreferences(context: Context) {
    private val sharedPrefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "MyAppPreferences"
        private const val DEFAULT_LANGUAGE_KEY = "default_language"
        private const val FIRST_TIME_USE = "is_first_time"
        private const val IS_TOUR_COMPLETE = "is_tour_complete"
        private const val IS_DB_INIT_KEY = "is_db_initialized"
        private const val IS_ACCOUNT_SETUP = "is_account_setup"
    }

    fun getDefaultLanguage(): String {
        return sharedPrefs.getString(DEFAULT_LANGUAGE_KEY, Language.English.isoCode) ?: Language.English.isoCode
    }
    fun setDefaultLanguage(languageCode: String) {
        sharedPrefs.edit().putString(DEFAULT_LANGUAGE_KEY, languageCode).apply()
    }

    fun getIsDbInitialized(): Boolean {
        return sharedPrefs.getBoolean(IS_DB_INIT_KEY, false)
    }
    fun getIsTourComplete(): Boolean {
        return sharedPrefs.getBoolean(IS_TOUR_COMPLETE, false)
    }
    fun getIsAccountSetUp(): Boolean {
        return sharedPrefs.getBoolean(IS_ACCOUNT_SETUP, false)
    }
    fun setIsAccountSetUp(isAccountSetUp: Boolean) {
        sharedPrefs.edit().putBoolean(IS_ACCOUNT_SETUP, isAccountSetUp).apply()
    }

    fun setIsDbInitialized(isInit: Boolean)  {
        sharedPrefs.edit().putBoolean(IS_DB_INIT_KEY, isInit).apply()
    }

    fun setIsFirstTimeLogin(isFirstTime: Boolean) {
        sharedPrefs.edit().putBoolean(FIRST_TIME_USE, isFirstTime).apply()
    }

    fun setIsTourComplete(isTourCompleted: Boolean){
        sharedPrefs.edit().putBoolean(IS_TOUR_COMPLETE, isTourCompleted).apply()
    }

    fun isAccountSetUp(isAccountSetUp: Boolean){
        sharedPrefs.edit().putBoolean(IS_ACCOUNT_SETUP, isAccountSetUp).apply()
    }


}