package com.example.parentcoachbot.feature_chat.domain.use_case.parentUserUseCases

import com.example.parentcoachbot.feature_chat.domain.repository.ParentUserRepository

class UpdateUsername (private val parentUserRepository: ParentUserRepository){
    suspend operator fun invoke(parentUserId: String, username: String){
        parentUserRepository.updateUsername(parentUserId, username)
    }

}