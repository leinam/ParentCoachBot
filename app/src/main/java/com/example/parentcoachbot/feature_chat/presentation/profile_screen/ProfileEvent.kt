package com.example.parentcoachbot.feature_chat.presentation.profile_screen

import com.example.parentcoachbot.feature_chat.domain.model.ChildProfile

sealed class ProfileEvent{
    data class selectProfile(val childProfile: ChildProfile): ProfileEvent()
}
