package com.example.parentcoachbot.feature_chat.domain.model

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Index
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Answer: RealmObject {
    var answerText: String? = null
    @Index
    var answerType: String? = null
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()
    var imagePath: String? = null
    var topics: RealmList<ObjectId> = realmListOf()
}