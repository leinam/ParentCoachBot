package com.example.parentcoachbot.feature_chat.domain.model

import io.realm.kotlin.types.RealmObject
import org.mongodb.kbson.ObjectId

class ParentUser: RealmObject {
    var _id: ObjectId = ObjectId.invoke()
    var name: String = "User"
    var username: String? = null
    var gender: String = Gender.NotSpecified.name
}

