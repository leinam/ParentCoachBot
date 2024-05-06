package com.example.parentcoachbot.feature_chat.data.repository

import com.example.parentcoachbot.feature_chat.domain.model.SearchQuery
import com.example.parentcoachbot.feature_chat.domain.repository.SearchQueryRepository
import io.realm.kotlin.Realm

class SearchQueryRepositoryImpl(private val realm: Realm): SearchQueryRepository {
    override suspend fun newSearchQuery(searchQuery: SearchQuery) {
        realm.write {
            this.copyToRealm(searchQuery)
        }
    }

}