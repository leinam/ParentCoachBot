package com.example.parentcoachbot.feature_chat.domain.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.parentcoachbot.R

sealed class Language (val isoCode: String,
                       @StringRes val name: Int,
                       @DrawableRes val icon: Int = R.drawable.baseline_language_24){
    object English : Language( "en", R.string.english_lang, R.drawable.icons8_great_britain_96)
    object Zulu : Language( "zu", R.string.zulu_lang, R.drawable.icons8_south_africa_96)
    object Portuguese : Language("pt", R.string.portuguese_lang, R.drawable.icons8_portugal_96)
}
