package com.example.parentcoachbot.feature_chat.domain.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class UserPreferences: RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()
    var parentUser: ObjectId? = null
    var defaultLanguage: String? = null
    var monitorUsage: Boolean = true
}