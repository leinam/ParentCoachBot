package com.example.parentcoachbot

import android.app.Application
import com.example.parentcoachbot.feature_chat.domain.util.AppPreferences
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp: Application(){
    lateinit var appPreferences: AppPreferences

    override fun onCreate() {
        super.onCreate()

        val config = applicationContext.resources.configuration
        appPreferences = AppPreferences(applicationContext)
        // appPreferences.setDefaultLanguage(config.locales.get(0).language)
    }

}