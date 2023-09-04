package com.example.parentcoachbot.feature_chat.domain.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Ignore
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import java.util.Date

class ChildProfile: RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()
    var parentUser: ObjectId? = null
    var name:String? = null
    var gender: String = Gender.NotSpecified.name
    @Ignore var dateOfBirth: Date? = null //TODO fix Date

}

enum class Gender(var description: String) {
    Male(description = "Male"),
    Female(description = "Female"),
    NotSpecified(description = "NotSpecified")
}



