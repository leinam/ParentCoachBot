package com.example.parentcoachbot.feature_chat.domain.model

import com.example.parentcoachbot.feature_chat.domain.util.AnswerType
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Index
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Answer: RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()
    var supportingImagePath: String? = null
    var answerTextEn: String? = null
    var answerTextPt: String? = null
    var answerTextZu: String? = null
    var answerTextAfr: String? = null
    @Index
    var answerType: String? = AnswerType.Info.code
    var answerThread: String? = null
    var answerThreadPosition: Int? = null
}