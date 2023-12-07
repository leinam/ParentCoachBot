package com.example.parentcoachbot.feature_chat.domain.util

import com.example.parentcoachbot.feature_chat.domain.model.Question


interface QuestionSearcher {
    fun search(queryText: String,
               currentLanguage: String): List<String>
    fun populateIndex(questionsList: List<Question>,
                      currentLanguage: String)

}