package com.example.parentcoachbot.feature_chat.domain.model

import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Index
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class ChatSession: RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()
    var chatTitle: String = "New Chat"
    var childProfile: ObjectId? = null
    var timeStarted: RealmInstant = RealmInstant.now()
    var timeLastUpdated: RealmInstant? = timeStarted
    @Index
    var isPinned: Boolean = false
}