package com.example.parentcoachbot.feature_chat.domain.repository

import com.example.parentcoachbot.feature_chat.domain.model.Question
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {
    suspend fun getAllQuestions(): Flow<List<Question>>?

    suspend fun getQuestionsFromIdList(idList: List<String>): List<Question>?

    suspend fun getQuestionById(id: String): Question?

    suspend fun insertQuestion(question: Question)

    suspend fun deleteQuestion(id: String)

    suspend fun getQuestionsBySubtopic(subtopicCode: String): Flow<List<Question>>?

    suspend fun getQuestionsBySubtopicList(subtopicId: String): Flow<List<Question>>?

}