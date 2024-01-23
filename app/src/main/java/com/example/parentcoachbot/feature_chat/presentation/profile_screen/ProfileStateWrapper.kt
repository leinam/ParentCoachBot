package com.example.parentcoachbot.feature_chat.presentation.profile_screen

import com.example.parentcoachbot.feature_chat.domain.model.ChildProfile
import com.example.parentcoachbot.feature_chat.domain.model.ParentUser
import com.example.parentcoachbot.feature_chat.domain.util.AppPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ProfileStateWrapper(
    val parentUserState: StateFlow<ParentUser?> = MutableStateFlow(null),
    var currentChildProfileState: StateFlow<ChildProfile?> = MutableStateFlow(null),
    val childProfilesListState: StateFlow<List<ChildProfile>> = MutableStateFlow(emptyList()),
    val currentLanguageCode: StateFlow<String?> = MutableStateFlow(null),
    val appPreferences: StateFlow<AppPreferences?> = MutableStateFlow(null)
) {
}