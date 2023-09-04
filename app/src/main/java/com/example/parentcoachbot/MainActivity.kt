package com.example.parentcoachbot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.parentcoachbot.feature_chat.presentation.Navigation
import com.example.parentcoachbot.ui.theme.ParentCoachBotTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ParentCoachBotTheme {
                // A surface container using the 'background' color from the theme
                println("this is a nice day to log")
                Navigation()

            }
        }
    }
}




