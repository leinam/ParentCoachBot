package com.example.parentcoachbot.feature_chat.domain.model

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Question: RealmObject{
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()
    var questionText:String? = null
    var questionAnswers: RealmList<ObjectId> = realmListOf()
    var searchKeywords: RealmList<String> = realmListOf()
    var subtopics: RealmList<ObjectId> = realmListOf()
    var tags: RealmList<String> = realmListOf()
    var timeCreated: RealmInstant = RealmInstant.now()
}

class InvalidQuestionException(message:String): Exception(message){

}


