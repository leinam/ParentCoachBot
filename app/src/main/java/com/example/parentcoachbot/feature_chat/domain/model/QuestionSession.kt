package com.example.parentcoachbot.feature_chat.domain.model

import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class QuestionSession: RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()
    var chatSession: ObjectId? = null
    var childProfile: ObjectId? = null
    var question:ObjectId? = null
    var timeAsked: RealmInstant = RealmInstant.now()
    var isSaved: Boolean = false
}

