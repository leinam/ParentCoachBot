package com.example.parentcoachbot.feature_chat.domain.use_case.searchQueryUseCases

import com.example.parentcoachbot.feature_chat.domain.model.SearchQuery
import com.example.parentcoachbot.feature_chat.domain.repository.SearchQueryRepository

class NewSearchQuery(private val searchQueryRepository: SearchQueryRepository) {
    suspend operator fun invoke(searchQuery: SearchQuery){
        searchQueryRepository.newSearchQuery(searchQuery)
    }


}