package com.example.parentcoachbot.feature_chat.domain.model


interface QuestionSearcher {
    fun search(queryText: String,
               currentLanguage: String): List<String>
    fun populateIndex(questionsList: List<Question>,
                      currentLanguage: String)

}