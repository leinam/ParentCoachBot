package com.example.parentcoachbot.feature_chat.domain.repository

import com.example.parentcoachbot.feature_chat.domain.model.SearchQuery

interface SearchQueryRepository {
    suspend fun newSearchQuery(searchQuery: SearchQuery)
}