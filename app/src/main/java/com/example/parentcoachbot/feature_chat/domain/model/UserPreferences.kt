package com.example.parentcoachbot.feature_chat.domain.model

import com.example.parentcoachbot.feature_chat.domain.util.Language
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class UserPreferences: RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()
    var _partition: String? = null
    var parentUser: ObjectId? = null
    var defaultLanguage: String? = Language.English.isoCode
    var monitorUsage: Boolean = true
}