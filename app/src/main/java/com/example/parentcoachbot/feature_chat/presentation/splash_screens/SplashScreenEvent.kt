package com.example.parentcoachbot.feature_chat.presentation.splash_screens

sealed class SplashScreenEvent{
    data class TourCompleted(val isCompleted: Boolean): SplashScreenEvent()
}
