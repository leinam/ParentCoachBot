package com.example.parentcoachbot.feature_chat.presentation.profile_screen

import com.example.parentcoachbot.feature_chat.domain.model.ChildProfile

sealed class ProfileEvent{
    data class SelectProfile(val childProfile: ChildProfile): ProfileEvent()
    data class NewProfile(val childProfile: ChildProfile): ProfileEvent()
}
