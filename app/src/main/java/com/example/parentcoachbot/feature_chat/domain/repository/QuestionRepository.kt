package com.example.parentcoachbot.feature_chat.domain.repository

import com.example.parentcoachbot.feature_chat.domain.model.Question
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface QuestionRepository {
    suspend fun getAllQuestions(): Flow<List<Question>>

    suspend fun getQuestionsFromIdList(idList: List<ObjectId>): List<Question>

    suspend fun getQuestionById(id: ObjectId): Question?

    suspend fun insertQuestion(question: Question)

    suspend fun deleteQuestion(id: ObjectId)

    suspend fun getQuestionsBySubtopic(subtopicId: ObjectId): Flow<List<Question>>

}