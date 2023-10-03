package com.example.parentcoachbot.feature_chat.domain.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Index
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Answer: RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()
    var supportingImagePath: String? = null
    var answerText: String? = null
    @Index
    var answerType: String? = null
    var answerThread: String? = null
    var answerThreadPosition: Int? = null
}