package com.example.parentcoachbot.feature_chat.domain.model

import org.mongodb.kbson.ObjectId

interface QuestionSearcher {
    fun search(queryText: String,
               currentLanguage: String): List<ObjectId>
    fun populateIndex(questionsList: List<Question>,
                      currentLanguage: String)

}