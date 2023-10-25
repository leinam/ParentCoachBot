package com.example.parentcoachbot.feature_chat.domain.model

import io.realm.kotlin.ext.realmDictionaryOf
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmDictionary
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Question: RealmObject{
    @PrimaryKey
    var _id: ObjectId = ObjectId.invoke()
    var questionText:RealmDictionary<String?> = realmDictionaryOf()
    var answerThread: String? = null
    var searchKeywords: RealmList<String> = realmListOf()
    var subtopics: RealmList<ObjectId> = realmListOf()
    var subtopic: String? = null
    var tags: RealmList<String> = realmListOf()
    var timeCreated: RealmInstant = RealmInstant.now()
}

class InvalidQuestionException(message:String): Exception(message){

}


