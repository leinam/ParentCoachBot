package com.example.parentcoachbot.feature_chat.domain.repository

import com.example.parentcoachbot.feature_chat.domain.model.ChildProfile
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface ChildProfileRepository {
    suspend fun newChildProfile(childProfile: ChildProfile)

    suspend fun getChildProfileByParent(parentId: ObjectId): Flow<List<ChildProfile>>

    suspend fun getChildProfileByParentTest(parentId: ObjectId): ChildProfile

    suspend fun getChildProfileById(id: ObjectId): ChildProfile

    suspend fun deleteChildProfile(id: ObjectId)

}