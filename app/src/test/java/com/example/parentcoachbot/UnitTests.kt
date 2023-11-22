
import com.example.parentcoachbot.feature_chat.data.repository.AnswerRepositoryImpl
import com.example.parentcoachbot.feature_chat.domain.model.Answer
import com.example.parentcoachbot.feature_chat.domain.repository.AnswerRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.types.RealmList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class AnswerRepositoryImplTest {

    @Test
    fun getAnswers() = runBlocking {
        // Create a mock AnswerUseCases.
        //val mockAnswerUseCases = mock(AnswerUseCases::class.java)

        // Create a mock GetAnswersByIdList.
        //val mockGetAnswersByIdList = mock(GetAnswersByIdList::class.java)
        val mockGetAnswersByIdList = mock(GetAnswersByIdListInterface::class.java)

        // Set up Realm configuration for in-memory database.
        val realmConfig = RealmConfiguration.Builder().inMemory().name("test-realm").build()

        // Open a Realm instance for testing.
        //val testRealm = Realm.getInstance(realmConfig)
        val testRealm = Realm.open(realmConfig)

        // Create a mock AnswerRepositoryImpl with the Realm instance.
        val answerRepositoryImpl = AnswerRepositoryImpl(testRealm)

        // Create a list of answers.
        val answers = listOf(
            Answer(1, "Answer 1"),
            Answer(2, "Answer 2"),
            Answer(3, "Answer 3")
        )

        // Assuming you have a function to get the question IDs or you can use a list directly
        val questionIdsList = listOf("1", "2", "3")

        // Initialize a RealmList<String> using realmListOf extension function
        val questionIds: RealmList<String> = questionIdsList.toRealmList()

        // Set up the mock AnswerUseCases to return the list of answers.
       // `when`(mockAnswerUseCases.getAnswersByIdList).thenReturn(answers)

        // Set up the mock GetAnswersByIdList to return the list of answers when invoked with questionIdsList.
        `when`(mockGetAnswersByIdList.invoke(questionIds)).thenReturn(answers)



        // Call the getAnswers() method on the AnswerRepositoryImpl.
        val actualAnswers = answerRepositoryImpl.getQuestionAnswers(questionIds)

        // Assert that the actual answers are equal to the expected answers.
        assertEquals(answers, actualAnswers)

        // Close the test Realm instance.
        testRealm.close()
    }
}

fun RealmConfiguration.Companion.Builder(): RealmConfiguration.Builder {
    return RealmConfiguration.Builder()
}

interface GetAnswersByIdListInterface {
    suspend operator fun invoke(questionAnswers: RealmList<String>): List<Answer>
}

class GetAnswersByIdList(private val repository: AnswerRepository) : GetAnswersByIdListInterface {
    override suspend operator fun invoke(questionAnswers: RealmList<String>): List<Answer> {
        return repository.getQuestionAnswers(idList = questionAnswers)
    }
}
