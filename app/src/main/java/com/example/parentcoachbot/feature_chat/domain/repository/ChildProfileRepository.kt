package com.example.parentcoachbot.feature_chat.domain.repository

import com.example.parentcoachbot.feature_chat.domain.model.ChildProfile
import kotlinx.coroutines.flow.Flow

interface ChildProfileRepository {
    suspend fun newChildProfile(childProfile: ChildProfile)
    suspend fun getChildProfileByParent(parentId: String): Flow<List<ChildProfile>>?
    suspend fun getAllChildProfiles(): Flow<List<ChildProfile>>?

    suspend fun getChildProfileByParentTest(parentId: String): ChildProfile?

    suspend fun getChildProfileById(id: String): ChildProfile?

    suspend fun deleteChildProfile(id: String)

}