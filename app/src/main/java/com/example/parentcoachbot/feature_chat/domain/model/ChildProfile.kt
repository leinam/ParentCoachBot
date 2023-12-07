package com.example.parentcoachbot.feature_chat.domain.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

class ChildProfile : RealmObject {
    @PrimaryKey
    var _id: String = UUID.randomUUID().toString()
    var _partition: String? = null
    var parentUser: String? = null
    var name: String? = null
    var gender: String = Sex.NotSpecified.name
    var yearOfBirth: Int? = null
    var monthOfBirth: Int? = null

}

enum class Sex(var description: Map<String, String>) {
    Male(
        description = mapOf(
            Pair("en", "Male"),
            Pair("zu", "Owesilisa"),
            Pair("pt", "Macho")
        )
    ),
    Female(
        description = mapOf(
            Pair("en", "Female"),
            Pair("zu", "Owesifazane"),
            Pair("pt", "Fêmea")
        )
    ),
    NotSpecified(
        description = mapOf(
            Pair("en", "Not Specified"),
            Pair("zu", "Kungcono ungasho"),
            Pair("pt", "Prefiro não dizer")
        )
    )
}

sealed class Month(number: String, name: String) {

}



