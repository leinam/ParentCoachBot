package com.example.parentcoachbot.feature_chat.domain.util

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

class MyContextWrapper(base: Context) : ContextWrapper(base) {
    companion object {
        fun wrap(context: Context, language: String): ContextWrapper {
            val config = Configuration()
            config.setToDefaults()
            config.setLocale(Locale(language))

            // config = context.resources.configuration

            val sysLocale: Locale =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) config.locales.get(0) else config.locale


            if (language != "" && sysLocale.language != language) {
                val locale = Locale(language)
                Locale.setDefault(locale)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    config.setLocale(locale)
                } else {
                    config.locale = locale
                }
            }

            val newBase = context.createConfigurationContext(config)
            return MyContextWrapper(newBase)
        }
    }


}




