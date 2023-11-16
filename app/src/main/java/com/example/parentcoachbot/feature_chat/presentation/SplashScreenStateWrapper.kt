package com.example.parentcoachbot.feature_chat.presentation

import android.app.Application
import com.example.parentcoachbot.feature_chat.domain.util.AuthResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class SplashScreenStateWrapper(
    val authenticationResultState: StateFlow<AuthResult?> = MutableStateFlow(null
    ), val application: StateFlow<Application?> = MutableStateFlow(null)
)
