package com.example.parentcoachbot.feature_chat.domain.use_case.parentUserUseCases

import com.example.parentcoachbot.feature_chat.domain.repository.ParentUserRepository

class UpdateCountry (private val parentUserRepository: ParentUserRepository){
    suspend operator fun invoke(parentUserId: String, country: String){
        parentUserRepository.updateCountry(parentUserId, country)
    }

}