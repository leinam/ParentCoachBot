package com.example.parentcoachbot.feature_chat.domain.model

import io.realm.kotlin.ext.realmDictionaryOf
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmDictionary
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

class Question: RealmObject {
    @PrimaryKey
    var _id: String = UUID.randomUUID().toString()
    var questionText: RealmDictionary<String> = realmDictionaryOf()
    var owner_id: String? = null
    var answerThread: String? = null
    var questionCode: String? = null
    var searchKeywords: RealmList<String> = realmListOf()
    var subtopics: RealmList<String> = realmListOf()
    var subtopic: String? = null
    var tags: RealmList<String> = realmListOf()
}

class InvalidQuestionException(message:String): Exception(message){

}


