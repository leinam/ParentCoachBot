package com.example.parentcoachbot.feature_chat.data.repository

import com.example.parentcoachbot.feature_chat.domain.model.Answer
import com.example.parentcoachbot.feature_chat.domain.model.ChatSession
import com.example.parentcoachbot.feature_chat.domain.model.Subtopic
import com.example.parentcoachbot.feature_chat.domain.repository.ChatSessionRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmDictionaryOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId

class ChatSessionRepositoryImpl(private val realm: Realm) : ChatSessionRepository {
    override suspend fun newChatSession(chatSession: ChatSession) {
        realm.write {
            this.copyToRealm(chatSession)
        }
    }

    override suspend fun togglePinChatSession(id: ObjectId): Unit = withContext(Dispatchers.IO) {
        realm.write {
            val chatSession = realm.query<ChatSession>(query = "_id == $0", id).find().firstOrNull()

            chatSession?.let {
                val isPinned = !chatSession.isPinned
                chatSession.isPinned = false
            }
        }

    }

    override suspend fun deleteChatSession(id: ObjectId) {
        realm.write {
            val chatSession = realm.query<ChatSession>(query = "_id == $0", id).find().firstOrNull()
            chatSession?.let {
                findLatest(chatSession)?.also { delete(it) }
            }

        }
    }


    override suspend fun getChatSessionById(id: ObjectId): ChatSession? {
        return realm.query<ChatSession>(query = "_id == $0", id).find().firstOrNull()
    }

    override suspend fun getChatSessionsByChildProfile(childProfileId: ObjectId): Flow<List<ChatSession>> =
        withContext(Dispatchers.IO) {
            realm.query<ChatSession>(query = "childProfile == $0", childProfileId)
                .find()
                .asFlow()
                .map {
                    it.list.sortedByDescending { chatSession ->
                        chatSession.timeLastUpdated
                    }
                }

        }

    override suspend fun getChatSessionsByChildProfileAsynch(
        childProfileId: ObjectId
    ): Flow<ChatSession> = withContext(Dispatchers.IO)
    {
        realm.query<ChatSession>(query = "childProfile == $0", childProfileId)
            .find()
            .map { it }
            .asFlow()
    }

    override suspend fun updateLastAnswerText(answer: Answer, chatSessionId: ObjectId) {
        realm.write {
            val chatSession: ChatSession? =
                this.query<ChatSession>("_id == $0", chatSessionId).first().find()

            chatSession?.lastAnswerText = realmDictionaryOf(
                Pair("en", answer.answerTextEn),
                Pair("pt", answer.answerTextPt),
                Pair("zu", answer.answerTextZu),
            )
        }
    }

    override suspend fun updateChatTitle(subtopic: Subtopic, chatSessionId: ObjectId) {
        realm.write {
            val chatSession: ChatSession? =
                this.query<ChatSession>("_id == $0", chatSessionId).first().find()

            chatSession?.chatTitle = realmDictionaryOf(
                Pair("en", subtopic.titleEn),
                Pair("pt", subtopic.titlePt),
                Pair("zu", subtopic.titleZu)
            )
        }
    }
}