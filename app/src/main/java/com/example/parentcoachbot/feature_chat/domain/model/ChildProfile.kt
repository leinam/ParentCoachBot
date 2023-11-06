package com.example.parentcoachbot.feature_chat.domain.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class ChildProfile: RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()
    var _partition: String? = null
    var parentUser: ObjectId? = null
    var name:String? = null
    var gender: String = Sex.NotSpecified.name
    var yearOfBirth: Int? = null
    var monthOfBirth: Int? = null

}

enum class Sex(var description: String) {
    Male(description = "Male"),
    Female(description = "Female"),
    NotSpecified(description = "Rather Not Say")
}

sealed class Month(number: String, name: String){

}



