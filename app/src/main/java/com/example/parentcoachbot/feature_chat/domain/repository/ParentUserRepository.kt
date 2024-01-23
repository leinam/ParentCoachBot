package com.example.parentcoachbot.feature_chat.domain.repository

import com.example.parentcoachbot.feature_chat.domain.model.ParentUser
import kotlinx.coroutines.flow.Flow

interface ParentUserRepository {
    suspend fun newParentUser(parent: ParentUser)

    suspend fun deleteParentUser(id: String)

    suspend fun getParentUserById(id: String): ParentUser?

    fun getParentUser(): Flow<ParentUser?>?

    suspend fun updateUsername(id: String, username: String)

    suspend fun updateCountry(id: String, country: String)

}