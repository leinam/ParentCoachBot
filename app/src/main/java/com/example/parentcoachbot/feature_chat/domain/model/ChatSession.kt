package com.example.parentcoachbot.feature_chat.domain.model

import io.realm.kotlin.ext.realmDictionaryOf
import io.realm.kotlin.types.RealmDictionary
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Index
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

class ChatSession: RealmObject {
    @PrimaryKey
    var _id: String = UUID.randomUUID().toString()
    var _partition: String? = null
    var chatTitle: RealmDictionary<String> = realmDictionaryOf()
    var childProfile: String? = null
    var timeStarted: RealmInstant = RealmInstant.now()
    var timeLastUpdated: RealmInstant? = timeStarted
    @Index
    var isPinned: Boolean = false
    var lastAnswerText: RealmDictionary<String> = realmDictionaryOf()

}