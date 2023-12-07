package com.example.parentcoachbot.feature_chat.domain.model

import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

class QuestionSession: RealmObject {
    @PrimaryKey
    var _id: String = UUID.randomUUID().toString()
    var chatSession: String? = null
    var _partition: String? = null
    var childProfile: String? = null
    var question: String? = null
    var timeAsked: RealmInstant = RealmInstant.now()
    var isSaved: Boolean = false
    var timeSaved: RealmInstant? = null
}

