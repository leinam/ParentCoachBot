package com.example.parentcoachbot.feature_chat.presentation.profile_screen

import com.example.parentcoachbot.feature_chat.domain.model.ChildProfile
import com.example.parentcoachbot.feature_chat.domain.model.ParentUser

sealed class ProfileEvent {
    data class SelectProfile(val childProfile: ChildProfile) : ProfileEvent()
    data class UpdateProfileName(val childProfile: ChildProfile, val profileName: String): ProfileEvent()
    data class NewProfile(val childProfile: ChildProfile) : ProfileEvent()
    data class DeleteProfile(val childProfile: ChildProfile) : ProfileEvent()
    data class DeleteAllProfileData(val childProfile: ChildProfile) : ProfileEvent()
    data class UpdateUserAccount(val parentUser: ParentUser, val username: String, val country: String): ProfileEvent()
}
