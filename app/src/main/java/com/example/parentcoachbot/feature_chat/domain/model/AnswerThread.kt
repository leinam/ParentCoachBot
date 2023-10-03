package com.example.parentcoachbot.feature_chat.domain.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class AnswerThread: RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()
    var code: String? = null
    var title: String? = null
    var subtopic: ObjectId? = null
    var description: String? = null
}