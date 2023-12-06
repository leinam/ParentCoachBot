package com.example.parentcoachbot

import Builder
import com.example.parentcoachbot.feature_chat.data.repository.AnswerRepositoryImpl
import com.example.parentcoachbot.feature_chat.data.repository.AnswerThreadRepositoryImpl
import com.example.parentcoachbot.feature_chat.data.repository.QuestionRepositoryImpl
import com.example.parentcoachbot.feature_chat.domain.model.ChatSession
import com.example.parentcoachbot.feature_chat.domain.model.Question
import com.example.parentcoachbot.feature_chat.domain.model.QuestionSearcherImplementation
import com.example.parentcoachbot.feature_chat.domain.model.QuestionSession
import com.example.parentcoachbot.feature_chat.domain.repository.AnswerRepository
import com.example.parentcoachbot.feature_chat.domain.repository.AnswerThreadRepository
import com.example.parentcoachbot.feature_chat.domain.repository.QuestionRepository
import com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases.AddQuestion
import com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases.DeleteQuestion
import com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases.GetAllQuestions
import com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases.GetQuestionWithAnswers
import com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases.GetQuestionsBySubtopic
import com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases.GetQuestionsFromIdList
import com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases.GetQuestionsWithAnswers
import com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases.QuestionUseCases
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.types.RealmInstant
import junit.framework.TestCase.assertNotNull
import org.junit.Test

class QuestionSearchImplementationTest {

    @Test
    fun testChatSessionInstantiation() {

        // Create a ChatSession instance
        val chatSession = ChatSession()

        // Set properties as needed
        chatSession._id = "1"
        chatSession._partition = "1"
        chatSession.chatTitle.put("English", "Test")
        chatSession.childProfile = "Tariro"
        chatSession.timeStarted = RealmInstant.now()
        chatSession.timeLastUpdated = chatSession.timeStarted
        chatSession.isPinned = true
        chatSession.lastAnswerText.put("ENG", "Last Answer Text")

        // Now, you can use the chatSession object in your test
        assertNotNull(chatSession)
        // Add your assertions or test logic here


        // Create a QuestionSession instance
        val questionSession = QuestionSession()

        // Set properties as needed
        questionSession._id = "1"
        questionSession.chatSession = "1"
        questionSession._partition = "1"
        questionSession.childProfile = "Tariro"
        questionSession.question = "Should I use breast shells?"
        questionSession.timeAsked = RealmInstant.now()
        questionSession.isSaved = true // or false, depending on your test case

        // Now, you can use the questionSession object in your test
        assertNotNull(questionSession)
        // Add your assertions or test logic here

        // Set up Realm configuration for in-memory database.
        val realmConfig = RealmConfiguration.Builder().inMemory().name("test-realm").build()
        val testRealm = Realm.open(realmConfig)

        val questionRepository: QuestionRepository = QuestionRepositoryImpl(testRealm)
        val answerRepository: AnswerRepository = AnswerRepositoryImpl(testRealm)
        val answerThreadRepository: AnswerThreadRepository = AnswerThreadRepositoryImpl(testRealm)


        // Assuming you have the implementations for each use case
        val getAllQuestions = GetAllQuestions(questionRepository)
        val deleteQuestion = DeleteQuestion(questionRepository)
        val addQuestion = AddQuestion(questionRepository)
        val getQuestionsWithAnswers = GetQuestionsWithAnswers(questionRepository,answerRepository,answerThreadRepository )
        val getQuestionWithAnswers = GetQuestionWithAnswers(questionRepository,answerRepository,answerThreadRepository )
        val getQuestionBySubtopic = GetQuestionsBySubtopic(questionRepository)
        val getQuestionsFromIdList = GetQuestionsFromIdList(questionRepository)

// Initialize QuestionUseCases
        val questionUseCases = QuestionUseCases(
            getAllQuestions,
            deleteQuestion,
            addQuestion,
            getQuestionsWithAnswers,
            getQuestionWithAnswers,
            getQuestionBySubtopic,
            getQuestionsFromIdList
        )

        //create instance of questionSercher
    val questionSearcher = QuestionSearcherImplementation(questionUseCases)

//popuate index before runninq queries
        //where do I get the questions from?
        val questionsList: List<Question> = "Can I eat cheese?", "Should I use breast shells?"
        val currentLanguage: String = "English"
            questionSearcher.populateIndex(questionsList, currentLanguage)

        //run queries
        val queryText: String = "your_query_here"
        val searchResults: List<String> = questionSearcher.search(queryText, currentLanguage)

// Process the search results
        if (searchResults.isNotEmpty()) {
            println("Matching question IDs:")
            searchResults.forEach { questionId ->
                println(questionId)
            }
        } else {
            println("No matching questions found.")
        }


    }

}