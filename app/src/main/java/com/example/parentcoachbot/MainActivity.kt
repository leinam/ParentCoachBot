package com.example.parentcoachbot

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.parentcoachbot.feature_chat.domain.util.MyContextWrapper
import com.example.parentcoachbot.feature_chat.presentation.Navigation
import com.example.parentcoachbot.ui.theme.ParentCoachBotTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity() : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ParentCoachBotTheme {
                // A surface container using the 'background' color from the theme
                Navigation()
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val appPreferences = newBase.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val language = appPreferences.getString("default_language", "en") ?: "en"

        val context = MyContextWrapper.wrap(newBase,
            language = language)
        super.attachBaseContext(context)
    }
}




