package com.example.parentcoachbot.feature_chat.domain.repository

import com.example.parentcoachbot.feature_chat.domain.model.ParentUser
import org.mongodb.kbson.ObjectId

interface ParentUserRepository {
    suspend fun newParentUser(parent: ParentUser)

    suspend fun deleteParentUser(id: ObjectId)

    suspend fun getParentUserById(id: ObjectId): ParentUser?

    fun getParentUser(): ParentUser?

}