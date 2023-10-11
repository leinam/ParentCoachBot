package com.example.parentcoachbot.feature_chat.domain.util

sealed class Language (val isoCode: String, name: String){
    object English : Language( "en", "English")
    object Afrikaans : Language( "af", "Afrikaans")
    object Zulu : Language( "zu", "Zulu")
    object Portuguese : Language("pt", "Portuguese")
}
