package com.example.parentcoachbot.feature_chat.domain.repository

import com.example.parentcoachbot.feature_chat.domain.model.ParentUser

interface ParentUserRepository {
    suspend fun newParentUser(parent: ParentUser)

    suspend fun deleteParentUser(id: String)

    suspend fun getParentUserById(id: String): ParentUser?

    fun getParentUser(): ParentUser?

}