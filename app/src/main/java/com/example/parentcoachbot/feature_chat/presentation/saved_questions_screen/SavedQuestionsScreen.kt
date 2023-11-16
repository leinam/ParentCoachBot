import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.parentcoachbot.R
import com.example.parentcoachbot.feature_chat.domain.util.Language
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.AnswerBox
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.QuestionBox
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.TopNavBar
import com.example.parentcoachbot.feature_chat.presentation.saved_questions_screen.SavedQuestionStateWrapper
import com.example.parentcoachbot.ui.theme.BackgroundWhite
import com.example.parentcoachbot.ui.theme.PlexSans
import com.example.parentcoachbot.ui.theme.PrimaryGreen
import com.example.parentcoachbot.ui.theme.drawerItemsList
import kotlinx.coroutines.launch

@Preview
@Composable
fun SavedQuestionsScreen(
    navController: NavController = rememberNavController(),
    savedQuestionsViewModelState: State<SavedQuestionStateWrapper> = mutableStateOf(
        SavedQuestionStateWrapper()
    )
) {

    val savedQuestionsViewModelStateWrapper = savedQuestionsViewModelState.value
    val scope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()
    var drawerSelectedItemIndex by rememberSaveable { mutableIntStateOf(3) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val currentLanguageCode =
        savedQuestionsViewModelStateWrapper.currentLanguageCode.collectAsStateWithLifecycle()
    val currentChildProfile =
        savedQuestionsViewModelStateWrapper.currentChildProfile.collectAsStateWithLifecycle()
    val savedQuestionSessionsWithQuestionsAndAnswers =
        savedQuestionsViewModelStateWrapper.savedQuestionSessionsWithQuestionAndAnswersState.collectAsStateWithLifecycle()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet()
            {
                drawerItemsList.forEachIndexed { index, navBarItem ->
                    NavigationDrawerItem(
                        label = { navBarItem.title?.let { Text(text = stringResource(id = it)) } },
                        selected = index == drawerSelectedItemIndex,
                        onClick = {
                            drawerSelectedItemIndex = index
                            scope.launch {
                                drawerState.close()
                                navBarItem.route?.let { navController.navigate(route = it) }
                            }
                        },
                        icon = {
                            Icon(painter = painterResource(id = navBarItem.icon),
                                contentDescription = navBarItem.title?.let { stringResource(it) }
                            )
                        },
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }
    ) {
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = BackgroundWhite)
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
                            text = stringResource(id = R.string.saved_questions_title),
                            color = PrimaryGreen,
                            fontFamily = PlexSans,
                            fontWeight = FontWeight.Normal,
                            fontSize = 19.sp
                        )


                    }

                    LazyColumn(
                        state = scrollState,
                        modifier = Modifier.padding(bottom = 80.dp)
                    ) {
                        items(savedQuestionSessionsWithQuestionsAndAnswers.value) { questionSessionWithQuestionAndAnswer ->

                            questionSessionWithQuestionAndAnswer?.let { questionSessionAnswerTriple ->

                                questionSessionAnswerTriple.second?.let { question ->
                                    QuestionBox(
                                        question = question,
                                        questionSession = questionSessionAnswerTriple.first,
                                        currentLanguageCode = currentLanguageCode.value
                                            ?: Language.English.isoCode
                                    )
                                }

                                questionSessionAnswerTriple.third?.forEach { answer ->
                                    // todo check how long ago questions session was loaded
                                    AnswerBox(
                                        questionAnswer = answer,
                                        currentLanguageCode = currentLanguageCode.value
                                            ?: Language.English.isoCode
                                    )


                                }

                            }

                        }


                        item {
                            Box(modifier = Modifier.height(57.dp)) {

                            }
                        }
                    }


                }

            }
        }
    }
}
