package com.example.parentcoachbot.di
import com.example.parentcoachbot.common.GlobalState
import com.example.parentcoachbot.feature_chat.data.repository.AnswerRepositoryImpl
import com.example.parentcoachbot.feature_chat.data.repository.ChatSessionRepositoryImpl
import com.example.parentcoachbot.feature_chat.data.repository.ChildProfileRepositoryImpl
import com.example.parentcoachbot.feature_chat.data.repository.ParentUserRepositoryImpl
import com.example.parentcoachbot.feature_chat.data.repository.QuestionRepositoryImpl
import com.example.parentcoachbot.feature_chat.data.repository.QuestionSessionRepositoryImpl
import com.example.parentcoachbot.feature_chat.data.repository.SubtopicRepositoryImpl
import com.example.parentcoachbot.feature_chat.data.repository.TopicRepositoryImpl
import com.example.parentcoachbot.feature_chat.domain.model.Answer
import com.example.parentcoachbot.feature_chat.domain.model.ChatSession
import com.example.parentcoachbot.feature_chat.domain.model.ChildProfile
import com.example.parentcoachbot.feature_chat.domain.model.ParentUser
import com.example.parentcoachbot.feature_chat.domain.model.PopulateDb
import com.example.parentcoachbot.feature_chat.domain.model.Question
import com.example.parentcoachbot.feature_chat.domain.model.QuestionSession
import com.example.parentcoachbot.feature_chat.domain.model.Subtopic
import com.example.parentcoachbot.feature_chat.domain.model.Topic
import com.example.parentcoachbot.feature_chat.domain.repository.AnswerRepository
import com.example.parentcoachbot.feature_chat.domain.repository.ChatSessionRepository
import com.example.parentcoachbot.feature_chat.domain.repository.ChildProfileRepository
import com.example.parentcoachbot.feature_chat.domain.repository.ParentUserRepository
import com.example.parentcoachbot.feature_chat.domain.repository.QuestionRepository
import com.example.parentcoachbot.feature_chat.domain.repository.QuestionSessionRepository
import com.example.parentcoachbot.feature_chat.domain.repository.SubtopicRepository
import com.example.parentcoachbot.feature_chat.domain.repository.TopicRepository
import com.example.parentcoachbot.feature_chat.domain.use_case.parentUserUseCases.GetParentUser
import com.example.parentcoachbot.feature_chat.domain.use_case.parentUserUseCases.ParentUserUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases.AddQuestion
import com.example.parentcoachbot.feature_chat.domain.use_case.answerUseCases.AnswerUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.chatSessionUseCases.ChatSessionUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases.DeleteQuestion
import com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases.GetAllQuestions
import com.example.parentcoachbot.feature_chat.domain.use_case.topicUseCases.GetAllTopics
import com.example.parentcoachbot.feature_chat.domain.use_case.answerUseCases.GetAnswer
import com.example.parentcoachbot.feature_chat.domain.use_case.answerUseCases.GetAnswersByIdList
import com.example.parentcoachbot.feature_chat.domain.use_case.questionSessionUseCases.GetQuestionSessionsByChatSession
import com.example.parentcoachbot.feature_chat.domain.use_case.chatSessionUseCases.GetChatSessionById
import com.example.parentcoachbot.feature_chat.domain.use_case.chatSessionUseCases.GetChatSessionsByChildProfile
import com.example.parentcoachbot.feature_chat.domain.use_case.chatSessionUseCases.NewChatSession
import com.example.parentcoachbot.feature_chat.domain.use_case.childProfileUseCases.ChildProfileUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.childProfileUseCases.GetChildProfileById
import com.example.parentcoachbot.feature_chat.domain.use_case.childProfileUseCases.GetChildProfileTest
import com.example.parentcoachbot.feature_chat.domain.use_case.childProfileUseCases.GetChildProfilesByParentUser
import com.example.parentcoachbot.feature_chat.domain.use_case.questionSessionUseCases.GetLatestQuestionSessionByChat
import com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases.GetQuestionsWithAnswers
import com.example.parentcoachbot.feature_chat.domain.use_case.subtopicUseCases.GetSubtopicById
import com.example.parentcoachbot.feature_chat.domain.use_case.subtopicUseCases.GetSubtopicsByTopic
import com.example.parentcoachbot.feature_chat.domain.use_case.questionSessionUseCases.NewQuestionSession
import com.example.parentcoachbot.feature_chat.domain.use_case.questionSessionUseCases.QuestionSessionUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases.GetQuestionWithAnswers
import com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases.GetQuestionsBySubtopic
import com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases.QuestionUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.subtopicUseCases.SubtopicUseCases
import com.example.parentcoachbot.feature_chat.domain.use_case.topicUseCases.TopicUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRealmDatabase(): Realm{
        val config = RealmConfiguration.Builder(
            schema = setOf(Answer::class,
                Question::class, Topic::class,
                ChatSession::class, Subtopic::class,
            QuestionSession::class, ParentUser::class,
            ChildProfile::class))
            .name("PCdb")
            .schemaVersion(1)
            .compactOnLaunch()
            .initialData{
                println("Attempting to pre-populate database")
                PopulateDb(this)()
            }
            .build()

        return Realm.open(config)
    }

    @Provides
    @Singleton
    fun provideQuestionRepository(realm: Realm): QuestionRepository{
        return QuestionRepositoryImpl(realm = realm)
    }

    @Provides
    @Singleton
    fun provideAnswerRepository(realm: Realm): AnswerRepository{
        return AnswerRepositoryImpl(realm = realm)
    }

    @Provides
    @Singleton
    fun provideTopicRepository(realm: Realm): TopicRepository{
        return TopicRepositoryImpl(realm = realm)
    }

    @Provides
    @Singleton
    fun provideSubtopicRepository(realm: Realm): SubtopicRepository{
        return SubtopicRepositoryImpl(realm = realm)
    }

    @Provides
    @Singleton
    fun provideChatSessionRepository(realm: Realm): ChatSessionRepository{
        return ChatSessionRepositoryImpl(realm = realm)
    }

    @Provides
    @Singleton
    fun provideQuestionSessionRepository(realm: Realm): QuestionSessionRepository{
        return QuestionSessionRepositoryImpl(realm = realm)
    }

    @Provides
    @Singleton
    fun provideParentUserRepository(realm: Realm): ParentUserRepository{
        return ParentUserRepositoryImpl(realm = realm)
    }

    @Provides
    @Singleton
    fun provideChildProfileRepository(realm: Realm): ChildProfileRepository{
        return ChildProfileRepositoryImpl(realm = realm)
    }

    @Provides
    @Singleton
    fun provideQuestionUseCases(questionRepository: QuestionRepository,
                                answerRepository: AnswerRepository): QuestionUseCases {
        return QuestionUseCases(getAllQuestions = GetAllQuestions(questionRepository),
            deleteQuestion = DeleteQuestion(questionRepository),
            addQuestion = AddQuestion(questionRepository),
            getQuestionsWithAnswers = GetQuestionsWithAnswers(questionRepository, answerRepository),
            getQuestionWithAnswers = GetQuestionWithAnswers(questionRepository, answerRepository),
            getQuestionBySubtopic = GetQuestionsBySubtopic(questionRepository)
        )
    }

    @Provides
    @Singleton
    fun provideAnswerUseCases(answerRepository: AnswerRepository): AnswerUseCases {
        return AnswerUseCases(
            GetAnswer(answerRepository),
            GetAnswersByIdList(answerRepository))

    }

    @Provides
    @Singleton
    fun provideTopicUseCases(topicRepository: TopicRepository): TopicUseCases {
        return TopicUseCases(GetAllTopics(topicRepository))
    }

    @Provides
    @Singleton
    fun provideChatSessionUseCases(chatSessionRepository: ChatSessionRepository): ChatSessionUseCases {
        return ChatSessionUseCases(
            GetChatSessionsByChildProfile(chatSessionRepository),
            GetChatSessionById(chatSessionRepository),
            NewChatSession(chatSessionRepository)
        )
    }

    @Provides
    @Singleton
    fun provideQuestionSessionUseCases(questionSessionRepository: QuestionSessionRepository): QuestionSessionUseCases {
        return QuestionSessionUseCases(newQuestionSession = NewQuestionSession(questionSessionRepository),
            getChatQuestionSessions = GetQuestionSessionsByChatSession(questionSessionRepository),
            getLatestQuestionSessionByChat = GetLatestQuestionSessionByChat(questionSessionRepository)
        )
    }


    @Provides
    @Singleton
    fun provideSubtopicUseCases(subtopicRepository: SubtopicRepository): SubtopicUseCases {
        return SubtopicUseCases(getSubtopicsByTopic = GetSubtopicsByTopic(subtopicRepository),
            getSubtopicById = GetSubtopicById(subtopicRepository))
    }

    @Provides
    @Singleton
    fun provideChildProfileUseCases(childProfileRepository: ChildProfileRepository): ChildProfileUseCases {
        return ChildProfileUseCases(getChildProfileById = GetChildProfileById(childProfileRepository),
            getChildProfilesByParentUser = GetChildProfilesByParentUser(childProfileRepository),
            getChildProfileTest = GetChildProfileTest(childProfileRepository)
        )
    }

    @Provides
    @Singleton
    fun provideParentUserUseCases(parentUserRepository: ParentUserRepository): ParentUserUseCases {
        return ParentUserUseCases(getParentUser = GetParentUser(parentUserRepository))
    }

    @Provides
    @Singleton
    fun provideGlobalState(parentUserUseCases: ParentUserUseCases, childProfileUseCases: ChildProfileUseCases): GlobalState {
        return GlobalState(parentUserUseCases = parentUserUseCases,
            childProfileUseCases = childProfileUseCases)
    }

}