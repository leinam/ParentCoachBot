package com.example.parentcoachbot.feature_chat.presentation.profile_screen

import com.example.parentcoachbot.feature_chat.domain.model.ChildProfile
import com.example.parentcoachbot.feature_chat.domain.model.ParentUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ProfileStateWrapper(
    val parentUserState: StateFlow<ParentUser?> = MutableStateFlow(null),
    val currentChildProfileState: StateFlow<ChildProfile?> = MutableStateFlow(null),
    val childProfilesListState: StateFlow<List<ChildProfile>> = MutableStateFlow(emptyList()),
    val currentLanguageCode: StateFlow<String?> = MutableStateFlow(null)

) {
}