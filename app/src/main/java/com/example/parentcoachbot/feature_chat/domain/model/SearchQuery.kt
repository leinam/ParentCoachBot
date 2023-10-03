package com.example.parentcoachbot.feature_chat.domain.model

import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Index
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class SearchQuery: RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()
    var chatSession: ObjectId? = null
    var questionSelected: ObjectId? = null
    @Index
    var queryText: String? = null
    var timeQueried: RealmInstant = RealmInstant.now()
}