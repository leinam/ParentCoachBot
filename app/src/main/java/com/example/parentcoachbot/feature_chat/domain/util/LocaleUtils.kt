package com.example.parentcoachbot.feature_chat.domain.util

import android.content.Context
import java.util.Locale

object LocaleUtils {
    fun setLocale(c: Context, language: String = "en") {
        updateResources(c, language) // use locale codes
    }

    private fun updateResources(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val resources = context.resources
        val configuration = resources.configuration
        configuration.setLocale(locale)

        val displayMetrics = resources.displayMetrics
        val newContext = context.createConfigurationContext(configuration)

        // Now, update the configuration and resources
        resources.updateConfiguration(configuration, displayMetrics)

        // If you're using SharedPreferences or DataStore, you can save the selected language for future use here
    }
}




