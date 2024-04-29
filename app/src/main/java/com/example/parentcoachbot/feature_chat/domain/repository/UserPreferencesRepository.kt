package com.example.parentcoachbot.feature_chat.domain.repository

import com.example.parentcoachbot.feature_chat.domain.model.ParentUser
import com.example.parentcoachbot.feature_chat.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    suspend fun setDefaultLanguage(languageCode: String, parentUserId: String)
    suspend fun newUserPreference(parentUser: ParentUser)
    suspend fun getUserPreferencesByParentUser(parentUserId: String): Flow<UserPreferences?>?

}