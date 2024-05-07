package com.example.parentcoachbot.di

import android.app.Application
import com.example.parentcoachbot.common.EventLogger
import com.example.parentcoachbot.common.GlobalState
import com.example.parentcoachbot.feature_chat.data.repository.AnswerRepositoryImpl
import com.example.parentcoachbot.feature_chat.data.repository.AnswerThreadRepositoryImpl
import com.example.parentcoachbot.feature_chat.data.repository.ChatSessionRepositoryImpl
import com.example.parentcoachbot.feature_chat.data.repository.ChildProfileRepositoryImpl
import com.example.parentcoachbot.feature_chat.data.repository.ParentUserRepositoryImpl
import com.example.parentcoachbot.feature_chat.data.repository.QuestionRepositoryImpl
import com.example.parentcoachbot.feature_chat.data.repository.QuestionSessionRepositoryImpl
import com.example.parentcoachbot.feature_chat.data.repository.SearchQueryRepositoryImpl
import com.example.parentcoachbot.feature_chat.data.repository.SubtopicRepositoryImpl
import com.example.parentcoachbot.feature_chat.data.repository.TopicRepositoryImpl
import com.example.parentcoachbot.feature_chat.domain.repository.AnswerRepository
import com.example.parentcoachbot.feature_chat.domain.repository.AnswerThreadRepository
import com.example.parentcoachbot.feature_chat.domain.repository.ChatSessionRepository
import com.example.parentcoachbot.feature_chat.domain.repository.ChildProfileRepository
import com.example.parentcoachbot.feature_chat.domain.repository.ParentUserRepository
import com.example.parentcoachbot.feature_chat.domain.repository.QuestionRepository
import com.example.parentcoachbot.feature_chat.domain.repository.QuestionSessionRepository
import com.example.parentcoachbot.feature_chat.domain.repository.SearchQueryRepository
import com.example.parentcoachbot.feature_chat.domain.repository.SubtopicRepository
import com.example.parentcoachbot.feature_chat.domain.repository.TopicRepository
import com.example.parentcoachbot.feature_chat.domain.use_case.answerThreadUseCases.AnswerThreadUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.answerThreadUseCases.GetAnswerThreadByCode
import com.example.parentcoachbot.feature_chat.domain.use_case.answerThreadUseCases.GetAnswerThreadById
import com.example.parentcoachbot.feature_chat.domain.use_case.answerUseCases.AnswerUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.answerUseCases.GetAnswer
import com.example.parentcoachbot.feature_chat.domain.use_case.answerUseCases.GetAnswerThreadLastAnswer
import com.example.parentcoachbot.feature_chat.domain.use_case.answerUseCases.GetAnswersByIdList
import com.example.parentcoachbot.feature_chat.domain.use_case.chatSessionUseCases.ChatSessionUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.chatSessionUseCases.DeleteChatSession
import com.example.parentcoachbot.feature_chat.domain.use_case.chatSessionUseCases.GetChatSessionById
import com.example.parentcoachbot.feature_chat.domain.use_case.chatSessionUseCases.GetChatSessionsByChildProfile
import com.example.parentcoachbot.feature_chat.domain.use_case.chatSessionUseCases.NewChatSession
import com.example.parentcoachbot.feature_chat.domain.use_case.chatSessionUseCases.TogglePinChatSession
import com.example.parentcoachbot.feature_chat.domain.use_case.chatSessionUseCases.UpdateChatLastAnswerText
import com.example.parentcoachbot.feature_chat.domain.use_case.chatSessionUseCases.UpdateChatTimeLastUpdated
import com.example.parentcoachbot.feature_chat.domain.use_case.chatSessionUseCases.UpdateChatTitle
import com.example.parentcoachbot.feature_chat.domain.use_case.childProfileUseCases.ChildProfileUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.childProfileUseCases.DeleteAllProfileData
import com.example.parentcoachbot.feature_chat.domain.use_case.childProfileUseCases.DeleteChildProfile
import com.example.parentcoachbot.feature_chat.domain.use_case.childProfileUseCases.GetAllChildProfiles
import com.example.parentcoachbot.feature_chat.domain.use_case.childProfileUseCases.GetChildProfileById
import com.example.parentcoachbot.feature_chat.domain.use_case.childProfileUseCases.GetChildProfileTest
import com.example.parentcoachbot.feature_chat.domain.use_case.childProfileUseCases.GetChildProfilesByParentUser
import com.example.parentcoachbot.feature_chat.domain.use_case.childProfileUseCases.NewChildProfile
import com.example.parentcoachbot.feature_chat.domain.use_case.childProfileUseCases.UpdateProfileName
import com.example.parentcoachbot.feature_chat.domain.use_case.parentUserUseCases.GetParentUser
import com.example.parentcoachbot.feature_chat.domain.use_case.parentUserUseCases.ParentUserUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.parentUserUseCases.UpdateCountry
import com.example.parentcoachbot.feature_chat.domain.use_case.parentUserUseCases.UpdateUsername
import com.example.parentcoachbot.feature_chat.domain.use_case.questionSessionUseCases.DeleteQuestionSession
import com.example.parentcoachbot.feature_chat.domain.use_case.questionSessionUseCases.GetLatestQuestionSessionByChat
import com.example.parentcoachbot.feature_chat.domain.use_case.questionSessionUseCases.GetQuestionSessionsByChatSession
import com.example.parentcoachbot.feature_chat.domain.use_case.questionSessionUseCases.GetSavedQuestionSessionsByProfile
import com.example.parentcoachbot.feature_chat.domain.use_case.questionSessionUseCases.NewQuestionSession
import com.example.parentcoachbot.feature_chat.domain.use_case.questionSessionUseCases.QuestionSessionUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.questionSessionUseCases.ToggleSaveQuestionSession
import com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases.AddQuestion
import com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases.DeleteQuestion
import com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases.GetAllQuestions
import com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases.GetQuestionByCode
import com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases.GetQuestionById
import com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases.GetQuestionWithAnswers
import com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases.GetQuestionsBySubtopic
import com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases.GetQuestionsFromIdList
import com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases.GetQuestionsWithAnswers
import com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases.QuestionUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.searchQueryUseCases.NewSearchQuery
import com.example.parentcoachbot.feature_chat.domain.use_case.searchQueryUseCases.SearchQueryUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.subtopicUseCases.GetSubtopicByCode
import com.example.parentcoachbot.feature_chat.domain.use_case.subtopicUseCases.GetSubtopicById
import com.example.parentcoachbot.feature_chat.domain.use_case.subtopicUseCases.GetSubtopicsByTopic
import com.example.parentcoachbot.feature_chat.domain.use_case.subtopicUseCases.SubtopicUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.topicUseCases.GetAllTopics
import com.example.parentcoachbot.feature_chat.domain.use_case.topicUseCases.TopicUseCases
import com.example.parentcoachbot.feature_chat.domain.util.AppPreferences
import com.example.parentcoachbot.feature_chat.domain.util.NetworkConnectionMangerImpl
import com.example.parentcoachbot.feature_chat.domain.util.QuestionSearcher
import com.example.parentcoachbot.feature_chat.domain.util.QuestionSearcherImplementation
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideConnectivityManager(
        application: Application,
        coroutineScope: CoroutineScope
    ): NetworkConnectionMangerImpl {
        return NetworkConnectionMangerImpl(
            context = application.applicationContext,
            coroutineScope = coroutineScope
        )
    }

    @Provides
    @Singleton
    fun provideCoroutineScope(): CoroutineScope {
        return CoroutineScope(Dispatchers.Default)
    }

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(): FirebaseAnalytics {
        return Firebase.analytics
    }

    @Provides
    @Singleton
    fun provideEventLogger(firebaseAnalytics: FirebaseAnalytics): EventLogger {
        return EventLogger(firebaseAnalytics)
    }

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @Singleton
    @Provides
    fun provideDatabaseReference(database: FirebaseDatabase): DatabaseReference {
        return database.reference
    }

    @Provides
    @Singleton
    fun provideQuestionRepository(realm: Realm): QuestionRepository {
        return QuestionRepositoryImpl(realm = realm)
    }

    @Provides
    @Singleton
    fun provideAnswerRepository(realm: Realm): AnswerRepository {
        return AnswerRepositoryImpl(realm = realm)
    }

    @Provides
    @Singleton
    fun provideQuestionSearcher(questionUseCases: QuestionUseCases): QuestionSearcher {
        return QuestionSearcherImplementation(questionUseCases)
    }

    @Provides
    @Singleton
    fun provideTopicRepository(realm: Realm): TopicRepository {
        return TopicRepositoryImpl(realm = realm)
    }

    @Provides
    @Singleton
    fun provideSubtopicRepository(realm: Realm): SubtopicRepository {
        return SubtopicRepositoryImpl(realm = realm)
    }

    @Provides
    @Singleton
    fun provideChatSessionRepository(realm: Realm): ChatSessionRepository {
        return ChatSessionRepositoryImpl(realm = realm)
    }

    @Provides
    @Singleton
    fun provideAnswerThreadRepository(realm: Realm): AnswerThreadRepository {
        return AnswerThreadRepositoryImpl(realm = realm)
    }

    @Provides
    @Singleton
    fun provideQuestionSessionRepository(realm: Realm): QuestionSessionRepository {
        return QuestionSessionRepositoryImpl(realm = realm)
    }

    @Provides
    @Singleton
    fun provideParentUserRepository(realm: Realm): ParentUserRepository {
        return ParentUserRepositoryImpl(realm = realm)
    }

    @Provides
    @Singleton
    fun provideChildProfileRepository(realm: Realm): ChildProfileRepository {
        return ChildProfileRepositoryImpl(realm = realm)
    }

    @Provides
    @Singleton
    fun provideSearchQueryRepository(realm: Realm): SearchQueryRepository {
        return SearchQueryRepositoryImpl(realm = realm)
    }

    @Provides
    @Singleton
    fun provideQuestionUseCases(
        questionRepository: QuestionRepository,
        answerRepository: AnswerRepository,
        answerThreadRepository: AnswerThreadRepository
    ): QuestionUseCases {
        return QuestionUseCases(
            getAllQuestions = GetAllQuestions(questionRepository),
            deleteQuestion = DeleteQuestion(questionRepository),
            addQuestion = AddQuestion(questionRepository),
            getQuestionsWithAnswers = GetQuestionsWithAnswers(
                questionRepository,
                answerRepository,
                answerThreadRepository
            ),
            getQuestionWithAnswers = GetQuestionWithAnswers(
                questionRepository,
                answerRepository,
                answerThreadRepository
            ),
            getQuestionBySubtopic = GetQuestionsBySubtopic(questionRepository),
            getQuestionsFromIdList = GetQuestionsFromIdList(questionRepository = questionRepository),
            getQuestionById = GetQuestionById(repository = questionRepository),
            getQuestionByCode = GetQuestionByCode(repository = questionRepository)
        )
    }

    @Provides
    @Singleton
    fun provideAnswerUseCases(answerRepository: AnswerRepository): AnswerUseCases {
        return AnswerUseCases(
            GetAnswer(answerRepository),
            GetAnswersByIdList(answerRepository),
            GetAnswerThreadLastAnswer(answerRepository)
        )

    }

    @Provides
    @Singleton
    fun provideAnswerThreadUseCases(answerThreadRepository: AnswerThreadRepository): AnswerThreadUseCases {
        return AnswerThreadUseCases(
            getAnswerThreadByCode = GetAnswerThreadByCode(answerThreadRepository),
            getAnswerThreadById = GetAnswerThreadById(answerThreadRepository)
        )

    }

    @Provides
    @Singleton
    fun provideTopicUseCases(topicRepository: TopicRepository): TopicUseCases {
        return TopicUseCases(GetAllTopics(topicRepository))
    }

    @Provides
    @Singleton
    fun provideChatSessionUseCases(
        chatSessionRepository: ChatSessionRepository,
        questionSessionRepository: QuestionSessionRepository
    ): ChatSessionUseCases {
        return ChatSessionUseCases(
            GetChatSessionsByChildProfile(chatSessionRepository),
            GetChatSessionById(chatSessionRepository),
            NewChatSession(chatSessionRepository),
            TogglePinChatSession(chatSessionRepository),
            DeleteChatSession(chatSessionRepository, questionSessionRepository),
            UpdateChatLastAnswerText(chatSessionRepository),
            UpdateChatTitle(chatSessionRepository),
            UpdateChatTimeLastUpdated(chatSessionRepository)
        )
    }

    @Provides
    @Singleton
    fun provideQuestionSessionUseCases(questionSessionRepository: QuestionSessionRepository): QuestionSessionUseCases {
        return QuestionSessionUseCases(
            newQuestionSession = NewQuestionSession(questionSessionRepository),
            getChatQuestionSessions = GetQuestionSessionsByChatSession(questionSessionRepository),
            getLatestQuestionSessionByChat = GetLatestQuestionSessionByChat(
                questionSessionRepository
            ),
            toggleSaveQuestionSession = ToggleSaveQuestionSession(questionSessionRepository),
            deleteQuestionSession = DeleteQuestionSession(questionSessionRepository),
            getSavedQuestionSessionsByProfile = GetSavedQuestionSessionsByProfile(
                questionSessionRepository
            )
        )
    }


    @Provides
    @Singleton
    fun provideSubtopicUseCases(subtopicRepository: SubtopicRepository): SubtopicUseCases {
        return SubtopicUseCases(
            getSubtopicsByTopic = GetSubtopicsByTopic(subtopicRepository),
            getSubtopicById = GetSubtopicById(subtopicRepository),
            getSubtopicByCode = GetSubtopicByCode(subtopicRepository)
        )
    }

    @Provides
    @Singleton
    fun provideChildProfileUseCases(
        childProfileRepository: ChildProfileRepository,
        chatSessionUseCases: ChatSessionUseCases
    ): ChildProfileUseCases {
        return ChildProfileUseCases(
            getChildProfileById = GetChildProfileById(childProfileRepository),
            getChildProfilesByParentUser = GetChildProfilesByParentUser(childProfileRepository),
            getChildProfileTest = GetChildProfileTest(childProfileRepository),
            newChildProfile = NewChildProfile(childProfileRepository),
            getAllChildProfiles = GetAllChildProfiles(childProfileRepository),
            deleteChildProfile = DeleteChildProfile(childProfileRepository, chatSessionUseCases),
            deleteAllProfileData = DeleteAllProfileData(chatSessionUseCases),
            updateProfileName = UpdateProfileName(childProfileRepository)
        )
    }

    @Provides
    @Singleton
    fun provideParentUserUseCases(parentUserRepository: ParentUserRepository): ParentUserUseCases {
        return ParentUserUseCases(
            getParentUser = GetParentUser(parentUserRepository),
            updateCountry = UpdateCountry(parentUserRepository),
            updateUsername = UpdateUsername(parentUserRepository)
        )
    }

    @Provides
    @Singleton
    fun provideSearchQueryUserUseCases(searchQueryRepository: SearchQueryRepository): SearchQueryUseCases {
        return SearchQueryUseCases(NewSearchQuery(searchQueryRepository))
    }

    @Provides
    @Singleton
    fun provideAppPreferences(application: Application): AppPreferences {
        return AppPreferences(application.applicationContext)
    }

    @Provides
    @Singleton
    fun provideGlobalState(
        parentUserUseCases: ParentUserUseCases,
        childProfileUseCases: ChildProfileUseCases,
        application: Application,
        firebaseAnalytics: FirebaseAnalytics,
        appPreferences: AppPreferences
    ): GlobalState {
        return GlobalState(
            parentUserUseCases = parentUserUseCases,
            appPreferences = appPreferences,
            firebaseAnalytics = firebaseAnalytics

        )
    }

}