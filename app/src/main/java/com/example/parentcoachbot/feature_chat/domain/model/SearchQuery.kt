package com.example.parentcoachbot.feature_chat.domain.model

import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Index
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

class SearchQuery: RealmObject {
    @PrimaryKey
    var _id: String = UUID.randomUUID().toString()
    var chatSession: String? = null
    var questionSelected: String? = null
    var _partition: String? = null
    @Index
    var queryText: String? = null
    var timeQueried: RealmInstant = RealmInstant.now()
}