import com.example.parentcoachbot.feature_chat.domain.model.Answer
import com.example.parentcoachbot.feature_chat.domain.model.Question
import com.example.parentcoachbot.feature_chat.domain.repository.AnswerRepository
import com.example.parentcoachbot.feature_chat.domain.repository.QuestionRepository
import com.example.parentcoachbot.feature_chat.domain.use_case.askQuestionUseCase.AskQuestionUseCase
import com.example.parentcoachbot.feature_chat.domain.use_case.answerUseCases.GetAnswer
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class AskQuestionAndReceiveAnswerTest {

    private lateinit var questionRepository: QuestionRepository
    private lateinit var answerRepository: AnswerRepository
    private lateinit var askQuestionUseCase: AskQuestionUseCase
    private lateinit var getAnswer: GetAnswer

    @Before
    fun setup() {
        // Initialize mock repositories and use cases
        questionRepository = mock(QuestionRepository::class.java)
        answerRepository = mock(AnswerRepository::class.java)
        askQuestionUseCase = AskQuestionUseCase(questionRepository)
        getAnswer = GetAnswer(answerRepository)
    }

    @Test
    fun `asking a question and receiving an answer works`() = runBlocking {
        // Set up test data
        val mockQuestion = Question(id = "1", text = "How are you?")
        val mockAnswer = Answer(id = "1", text = "I am fine.")

        // Set up expected behavior of dependencies
        `when`(questionRepository.askQuestion(any())).thenReturn(mockQuestion)
        `when`(answerRepository.getAnswerForQuestion(any())).thenReturn(mockAnswer)

        // Invoke the use case
        val question = askQuestionUseCase.askQuestion("How are you?")
        val answer = getAnswerUseCase.getAnswerForQuestion(question)

        // Assert the results
        assertEquals(mockQuestion, question)
        assertEquals(mockAnswer, answer)
    }
}
