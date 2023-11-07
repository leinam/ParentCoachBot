package com.example.parentcoachbot.feature_chat.domain.repository

import com.example.parentcoachbot.feature_chat.domain.model.AnswerThread

interface AnswerThreadRepository {

    suspend fun getAnswerThreadById(id: String): AnswerThread?

    suspend fun getAnswerThreadByCode(answerCode: String): AnswerThread?

    suspend fun deleteAnswerThread(id: String)

    suspend fun insertAnswerThread(answerThread: AnswerThread)


}

