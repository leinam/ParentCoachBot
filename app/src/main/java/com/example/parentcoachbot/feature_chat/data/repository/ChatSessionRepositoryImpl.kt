package com.example.parentcoachbot.feature_chat.data.repository

import android.util.Log
import com.example.parentcoachbot.feature_chat.domain.model.Answer
import com.example.parentcoachbot.feature_chat.domain.model.ChatSession
import com.example.parentcoachbot.feature_chat.domain.model.Subtopic
import com.example.parentcoachbot.feature_chat.domain.repository.ChatSessionRepository
import com.example.parentcoachbot.feature_chat.domain.util.Language
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.copyFromRealm
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmDictionaryOf
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ChatSessionRepositoryImpl(private val realm: Realm) : ChatSessionRepository {
    override suspend fun newChatSession(chatSession: ChatSession): Unit =
        withContext(Dispatchers.IO) {
            realm.write {
                this.copyToRealm(chatSession)
            }
            println("version no. " + realm.getNumberOfActiveVersions())
        }

    override suspend fun togglePinChatSession(id: String): Unit = withContext(Dispatchers.IO) {
        val chatSession = realm.query<ChatSession>(query = "_id == $0", id).find().firstOrNull()

        realm.write {
            chatSession?.let {
                findLatest(chatSession)?.let {
                    val isPinned = !it.isPinned
                    it.isPinned = isPinned
                    it.timePinned = if (isPinned) RealmInstant.now() else null

                }
            }

        }
    }

    override suspend fun deleteChatSession(id: String): Unit = withContext(Dispatchers.IO) {
        realm.write {
            val chatSession = realm.query<ChatSession>(query = "_id == $0", id).find().firstOrNull()
            chatSession?.let {
                findLatest(chatSession)?.also { delete(it) }
            }

        }
    }


    override suspend fun getChatSessionById(id: String): ChatSession? {
        return try {
            realm.query<ChatSession>(query = "_id == $0", id).find().firstOrNull()
                ?.copyFromRealm()
        } catch (e: Exception) {
            Log.println(Log.ERROR, "Realm", "An error occurred: ${e.message}}")
            null
        }
    }

    override suspend fun getChatSessionsByChildProfile(childProfileId: String): Flow<List<ChatSession>>? =
        withContext(Dispatchers.IO) {
            try {
                realm.query<ChatSession>(query = "childProfile == $0", childProfileId)
                    .find()
                    .asFlow()
                    .map { chatSessionResultsChange ->
                        chatSessionResultsChange.list.copyFromRealm()
                            .sortedWith(compareByDescending<ChatSession> { it.isPinned }.thenByDescending { it.timePinned }.thenByDescending { it.timeLastUpdated })
                    }
            } catch (e: Exception) {
                Log.println(Log.ERROR, "Realm", "An error occurred: ${e.message}}")
                null
            }

        }

    override suspend fun getChatSessionsByChildProfileAsynch(
        childProfileId: String
    ): Flow<ChatSession>? = withContext(Dispatchers.IO)
    {
        try {
            realm.query<ChatSession>(query = "childProfile == $0", childProfileId)
                .find()
                .map { it }
                .asFlow()
        } catch (e: Exception) {
            Log.println(Log.ERROR, "Realm", "An error occurred: ${e.message}}")
            null
        }
    }

    override suspend fun updateLastAnswerText(answer: Answer, chatSessionId: String): Unit =
        withContext(Dispatchers.IO) {
            realm.write {
                val chatSession: ChatSession? =
                    this.query<ChatSession>("_id == $0", chatSessionId).first().find()

                chatSession?.lastAnswerText = realmDictionaryOf(
                    Pair(
                        Language.English.isoCode,
                        answer.answerText[Language.English.isoCode] ?: ""
                    ),
                    Pair(
                        Language.Portuguese.isoCode,
                        answer.answerText[Language.Portuguese.isoCode] ?: ""
                    ),
                    Pair(Language.Zulu.isoCode, answer.answerText[Language.Zulu.isoCode] ?: "")
                )
            }
        }

    override suspend fun updateChatTitle(subtopic: Subtopic, chatSessionId: String): Unit =
        withContext(Dispatchers.IO) {
            realm.write {
                val chatSession: ChatSession? =
                    this.query<ChatSession>("_id == $0", chatSessionId).first().find()

                chatSession?.chatTitle = realmDictionaryOf(
                    Pair(Language.English.isoCode, subtopic.title[Language.English.isoCode] ?: ""),
                    Pair(
                        Language.Portuguese.isoCode,
                        subtopic.title[Language.Portuguese.isoCode] ?: ""
                    ),
                    Pair(Language.Zulu.isoCode, subtopic.title[Language.Zulu.isoCode] ?: "")
                )
            }
        }

    override suspend fun updateTimeLastUpdated(chatSessionId: String): Unit =
        withContext(Dispatchers.IO) {
            val chatSession: ChatSession? =
                realm.query<ChatSession>("_id == $0", chatSessionId).first().find()
            realm.write {
                chatSession?.let {
                    findLatest(it)?.let { liveChatSession ->
                        liveChatSession.timeLastUpdated = RealmInstant.now()

                    }
                }


            }
        }
}