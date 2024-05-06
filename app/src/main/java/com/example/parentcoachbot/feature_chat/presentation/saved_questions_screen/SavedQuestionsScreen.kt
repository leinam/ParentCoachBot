
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.parentcoachbot.MainActivity
import com.example.parentcoachbot.R
import com.example.parentcoachbot.feature_chat.domain.util.Language
import com.example.parentcoachbot.feature_chat.presentation.Screen
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.AnswerBox
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.CustomNavigationDrawer
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.DateBox
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.QuestionBox
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.TopNavBar
import com.example.parentcoachbot.feature_chat.presentation.saved_questions_screen.SavedQuestionStateWrapper
import com.example.parentcoachbot.feature_chat.presentation.saved_questions_screen.SavedQuestionsScreenEvent
import com.example.parentcoachbot.ui.theme.Beige
import com.example.parentcoachbot.ui.theme.LightBeige
import com.example.parentcoachbot.ui.theme.PlexSans
import com.example.parentcoachbot.ui.theme.PrimaryGreen
import com.example.parentcoachbot.ui.theme.TextGrey

@Preview
@Composable
fun SavedQuestionsScreen(
    navController: NavController = rememberNavController(),
    savedQuestionsViewModelState: State<SavedQuestionStateWrapper> = mutableStateOf(
        SavedQuestionStateWrapper()
    ),
    onEvent: (SavedQuestionsScreenEvent) -> Unit = {}
) {

    val savedQuestionsViewModelStateWrapper = savedQuestionsViewModelState.value
    val scope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()
    val drawerSelectedItemIndex = rememberSaveable { mutableIntStateOf(3) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val currentLanguageCode =
        savedQuestionsViewModelStateWrapper.currentLanguageCode.collectAsStateWithLifecycle()
    val currentChildProfile =
        savedQuestionsViewModelStateWrapper.currentChildProfile.collectAsStateWithLifecycle()
    val savedQuestionSessionsWithQuestionsAndAnswers =
        savedQuestionsViewModelStateWrapper.savedQuestionSessionsWithQuestionAndAnswersState.collectAsStateWithLifecycle()
    val savedQuestionSessionsWithQuestionsAndAnswersGroupedByDate =
        savedQuestionsViewModelStateWrapper.savedQuestionSessionsWithQuestionAndAnswersListGroupedByDateState.collectAsStateWithLifecycle()

    if (currentChildProfile == null){
        val intent =
            Intent(LocalContext.current, MainActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        LocalContext.current.startActivity(intent)
    }

    Scaffold(
        topBar = {
            TopNavBar(
                navController = navController,
                screenIndex = 2,
                drawerState = drawerState,
                scope = scope
            )
        },

        )
    { contentPadding ->
        CustomNavigationDrawer(
            drawerState = drawerState,
            drawerSelectedItemIndex = drawerSelectedItemIndex,
            navController = navController,
            currentChildProfileName = currentChildProfile.value?.name ?: "",
            contentPadding = contentPadding,
            content = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = LightBeige)
                        .padding(contentPadding)

                ) {

                    Column {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(PrimaryGreen.copy(alpha = 0.3f))
                                .padding(17.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = stringResource(id = R.string.saved_messages_label),
                                color = PrimaryGreen,
                                fontFamily = PlexSans,
                                fontWeight = FontWeight.Normal,
                                fontSize = 19.sp
                            )


                        }
                        if (savedQuestionSessionsWithQuestionsAndAnswers.value.isNotEmpty()) {
                            LazyColumn(
                                state = scrollState,
                                modifier = Modifier.padding(bottom = 80.dp)
                            ) {
                                savedQuestionSessionsWithQuestionsAndAnswersGroupedByDate.value.forEach {date, savedQuestionsTriple ->
                                    item {
                                        DateBox(
                                            date = date,
                                            currentLanguageCode = currentLanguageCode.value ?: "en"
                                        )
                                    }



                                    items(savedQuestionsTriple) { questionSessionWithQuestionAndAnswer ->

                                        questionSessionWithQuestionAndAnswer?.let { questionSessionAnswerTriple ->

                                            val isAnswerVisible = rememberSaveable {
                                                mutableStateOf(false)
                                            }

                                            questionSessionAnswerTriple.second?.let { question ->
                                                QuestionBox(
                                                    question = question,
                                                    questionSession = questionSessionAnswerTriple.first,
                                                    currentLanguageCode = currentLanguageCode.value
                                                        ?: Language.English.isoCode,
                                                    onSavedScreenEvent = onEvent,
                                                    screenName = Screen.SavedQuestionsScreen.route,
                                                    toggleAnswerVisibility = {
                                                        isAnswerVisible.value = !isAnswerVisible.value
                                                        println(isAnswerVisible.value)
                                                    }
                                                )
                                            }


                                                questionSessionAnswerTriple.third?.forEach { answer ->
                                                    // todo check how long ago questions session was loaded
                                                    AnimatedVisibility(visible = isAnswerVisible.value) {
                                                        AnswerBox(
                                                            questionAnswer = answer,
                                                            currentLanguageCode = currentLanguageCode.value
                                                                ?: Language.English.isoCode
                                                        )

                                                    }
                                                }



                                        }

                                    }
                                }


                            }
                        } else {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            )
                            {
                                Box(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(color = Beige)
                                        .padding(15.dp),

                                    ) {
                                    Text(
                                        text = stringResource(id = R.string.no_saved_messages_label).uppercase(),
                                        textAlign = TextAlign.Center, color = TextGrey
                                    )
                                }

                                Text(
                                    text = stringResource(id = R.string.no_saved_messages_prompt),
                                    textAlign = TextAlign.Center,
                                    color = PrimaryGreen,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(
                                        vertical = 8.dp,
                                        horizontal = 90.dp
                                    )
                                )


                            }
                        }


                    }

                }
            })

    }
}

