package com.example.parentcoachbot.feature_chat.presentation.chat_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.parentcoachbot.R
import com.example.parentcoachbot.feature_chat.domain.model.ChildProfile
import com.example.parentcoachbot.feature_chat.domain.model.Question
import com.example.parentcoachbot.feature_chat.domain.model.Subtopic
import com.example.parentcoachbot.feature_chat.domain.model.Topic
import com.example.parentcoachbot.feature_chat.domain.util.Language
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.AnswerBox
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.CustomNavigationDrawer
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.DateBox
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.QuestionBox
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.QuestionInputSection
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.TopNavBar
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.TypingAnimation
import com.example.parentcoachbot.ui.theme.BackgroundWhite
import com.example.parentcoachbot.ui.theme.LightBeige
import com.example.parentcoachbot.ui.theme.LightGreen
import com.example.parentcoachbot.ui.theme.LighterBeige
import com.example.parentcoachbot.ui.theme.ParentCoachBotTheme
import com.example.parentcoachbot.ui.theme.PrimaryGreen
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant

// todo modularize chat screen composable
@Preview
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ChatScreen(
    chatViewModelState: State<ChatStateWrapper> = mutableStateOf(ChatStateWrapper()),
    navController: NavController = rememberNavController(),
    onEvent: (chatEvent: ChatEvent) -> Unit = {}
) {

    val chatStateWrapper = chatViewModelState.value
    val keyboardController = LocalSoftwareKeyboardController.current
    val openAlertDialog = remember { mutableStateOf(false) }


    val topicsList: List<Topic> by chatStateWrapper.topicsListState.collectAsStateWithLifecycle()
    val questionSessionWithQuestionAndAnswersList by chatStateWrapper.questionSessionsWithQuestionAndAnswersState.collectAsStateWithLifecycle()
    val questionSessionWithQuestionAndAnswersListGroupedByDate by chatStateWrapper.questionSessionsWithQuestionAndAnswersGroupedByDateState.collectAsStateWithLifecycle()
    val subtopicList: List<Subtopic> by chatStateWrapper.subtopicsListState.collectAsStateWithLifecycle()
    val subtopicQuestionsList: List<Question> by chatStateWrapper.subtopicQuestionsListState.collectAsStateWithLifecycle()
    val searchResultQuestionsList: List<Question> by chatStateWrapper.searchResultsQuestionsListState.collectAsStateWithLifecycle()
    val currentChildProfile: ChildProfile? by chatStateWrapper.currentChildProfile.collectAsStateWithLifecycle()
    val currentLanguageCode: String? by chatStateWrapper.currentLanguageCode.collectAsStateWithLifecycle()
    var isAnswerVisible by remember { mutableStateOf(false) }
    val typingTransition: InfiniteTransition = rememberInfiniteTransition()


    val scope = rememberCoroutineScope()
    val drawerSelectedItemIndex = rememberSaveable {
        mutableIntStateOf(1)
    }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scrollState = rememberLazyListState()
    val isAnimationActive = remember {
        mutableStateOf(false)
    }


    val bottomSheetContentState: MutableStateFlow<BottomSheetContent> =
        MutableStateFlow(BottomSheetContent.SubTopics)
    val bottomSheetContent: BottomSheetContent by bottomSheetContentState.collectAsStateWithLifecycle()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState =
        rememberStandardBottomSheetState(
            initialValue =
            SheetValue.PartiallyExpanded, skipHiddenState = false
        )
    )

    var currentSubtopic: Subtopic? by remember { mutableStateOf(null) }


    ParentCoachBotTheme {
        Scaffold(
            bottomBar = {
                QuestionInputSection(
                    modifier = Modifier
                        .background(color = LightGreen)
                        .fillMaxWidth()
                        .padding(10.dp),
                    onEvent = onEvent, scope = scope,
                    bottomSheetContentState = bottomSheetContentState,
                    bottomSheetScaffoldState = bottomSheetScaffoldState
                )
            }, topBar = {
                TopNavBar(
                    drawerState = drawerState,
                    scope = scope,
                    navController = navController
                )
            })
        { contentPadding ->
            CustomNavigationDrawer(
                drawerState = drawerState,
                drawerSelectedItemIndex = drawerSelectedItemIndex,
                navController = navController,
                contentPadding = contentPadding,
                currentChildProfileName = currentChildProfile?.name ?: "",
                content = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = LightBeige)
                            .padding(contentPadding)
                    ) {

                        BottomSheetScaffold(
                            scaffoldState = bottomSheetScaffoldState,
                            sheetContent = {
                                when (bottomSheetContent) {
                                    BottomSheetContent.Questions -> {
                                        Column(modifier = Modifier.height(300.dp)) {
                                            Box(
                                                modifier = Modifier.fillMaxWidth(),
                                            ) {

                                                Icon(painter = painterResource(
                                                    id =
                                                    R.drawable.baseline_arrow_back_24
                                                ),
                                                    contentDescription = null,
                                                    tint = BackgroundWhite,
                                                    modifier = Modifier
                                                        .clickable {
                                                            bottomSheetContentState.value =
                                                                BottomSheetContent.SubTopics
                                                        }
                                                        .align(Alignment.CenterStart)
                                                        .padding(horizontal = 10.dp)
                                                )
                                                val currentSubtopicTitle =
                                                    currentSubtopic?.title?.get(currentLanguageCode)

                                                Text(
                                                    text = currentSubtopicTitle?.uppercase() ?: "",
                                                    color = LightBeige,
                                                    modifier = Modifier.align(Alignment.Center)
                                                )
                                            }

                                            Spacer(modifier = Modifier.size(10.dp))
                                            LazyColumn {

                                                items(subtopicQuestionsList) { question ->
                                                    Row(horizontalArrangement = Arrangement.SpaceBetween,
                                                        modifier = Modifier
                                                            .padding(16.dp)
                                                            .fillMaxWidth()
                                                            .clickable {
                                                                onEvent(
                                                                    ChatEvent.AddQuestionSession(
                                                                        question
                                                                    )
                                                                )
                                                                scope.launch {
                                                                    bottomSheetScaffoldState.bottomSheetState.partialExpand()
                                                                }

                                                                isAnswerVisible = true
                                                                isAnimationActive.value = true
                                                            }) {
                                                        val questionText =
                                                            question.questionText[currentLanguageCode]

                                                        questionText?.let {
                                                            Text(
                                                                text = it,
                                                                color = Color.White
                                                            )
                                                        }

                                                    }
                                                }
                                            }
                                        }
                                    }

                                    BottomSheetContent.SubTopics -> {
                                        Column(modifier = Modifier.height(300.dp)) {
                                            Box(modifier = Modifier.fillMaxWidth()) {
                                                Icon(painter = painterResource(
                                                    id = R.drawable.baseline_arrow_back_24
                                                ),
                                                    contentDescription = null,
                                                    tint = LightBeige,
                                                    modifier = Modifier
                                                        .clickable {
                                                            bottomSheetContentState.value =
                                                                BottomSheetContent.Topics
                                                        }
                                                        .align(Alignment.CenterStart)
                                                        .padding(horizontal = 10.dp))

                                                Text(
                                                    text = stringResource(
                                                        id = R.string.topics_label
                                                    ).uppercase(),
                                                    color = LightBeige, modifier =
                                                    Modifier.align(Alignment.Center),
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }

                                            Spacer(modifier = Modifier.size(10.dp))
                                            LazyColumn {
                                                items(subtopicList) { subtopic ->
                                                    Row(horizontalArrangement = Arrangement.SpaceBetween,
                                                        modifier = Modifier
                                                            .padding(16.dp)
                                                            .fillMaxWidth()
                                                            .clickable {
                                                                onEvent(
                                                                    ChatEvent.SelectSubtopic(
                                                                        subtopic
                                                                    )
                                                                )
                                                                currentSubtopic = subtopic
                                                                bottomSheetContentState.value =
                                                                    BottomSheetContent.Questions
                                                            }) {

                                                        val subtopicTitle =
                                                            subtopic.title[currentLanguageCode]
                                                                ?: subtopic.title[Language.English.isoCode]

                                                        Text(
                                                            text = subtopicTitle
                                                                ?: "",
                                                            color = Color.White
                                                        )


                                                        subtopic.icon?.let {
                                                            Icon(
                                                                painter = painterResource(id = subtopic.icon!!),
                                                                contentDescription = subtopicTitle,
                                                                tint = Color.White
                                                            )
                                                        }

                                                    }
                                                }
                                            }
                                        }
                                    }

                                    BottomSheetContent.Topics -> {
                                        Column(modifier = Modifier.height(300.dp)) {
                                            Box(
                                                modifier = Modifier.fillMaxWidth(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = stringResource(
                                                        id = R.string.topics_label
                                                    ).uppercase(),
                                                    color = LightBeige
                                                )
                                            }

                                            Spacer(modifier = Modifier.size(10.dp))

                                            LazyColumn {
                                                items(topicsList) { topic ->
                                                    val topicTitle =
                                                        topic.title[currentLanguageCode]

                                                    Row(horizontalArrangement = Arrangement.SpaceBetween,
                                                        modifier = Modifier
                                                            .padding(16.dp)
                                                            .fillMaxWidth()
                                                            .clickable {
                                                                onEvent(ChatEvent.SelectTopic(topic))
                                                                bottomSheetContentState.value =
                                                                    BottomSheetContent.SubTopics
                                                            }
                                                    ) {
                                                        topicTitle?.let {
                                                            Text(
                                                                text = it,
                                                                color = Color.White
                                                            )
                                                        }

                                                        topic.icon?.let {
                                                            Icon(
                                                                painter = painterResource(id = it),
                                                                contentDescription = topic.title[currentLanguageCode],
                                                                tint = Color.White
                                                            )
                                                        }

                                                    }
                                                }
                                            }
                                        }
                                    }

                                    BottomSheetContent.SearchResults -> {
                                        Column(modifier = Modifier.height(300.dp)) {
                                            Box(
                                                modifier = Modifier.fillMaxWidth(),
                                            ) {

                                                Icon(painter = painterResource(
                                                    id =
                                                    R.drawable.baseline_home_24
                                                ),
                                                    contentDescription = null,
                                                    tint = BackgroundWhite,
                                                    modifier = Modifier
                                                        .clickable {
                                                            bottomSheetContentState.value =
                                                                BottomSheetContent.Topics
                                                        }
                                                        .align(Alignment.CenterStart)
                                                        .padding(horizontal = 10.dp)
                                                )

                                                Text(
                                                    text = stringResource(id = R.string.search_results_label).uppercase(),
                                                    color = LightBeige,
                                                    modifier = Modifier.align(Alignment.Center)
                                                )
                                            }

                                            Spacer(modifier = Modifier.size(10.dp))

                                            if (searchResultQuestionsList.isNotEmpty()) {
                                                LazyColumn {
                                                    items(searchResultQuestionsList) { question ->
                                                        Row(horizontalArrangement = Arrangement.SpaceBetween,
                                                            modifier = Modifier
                                                                .padding(
                                                                    horizontal = 16.dp,
                                                                    vertical = 10.dp
                                                                )
                                                                .fillMaxWidth()
                                                                .clickable {
                                                                    onEvent(
                                                                        ChatEvent.AddQuestionSession(
                                                                            question
                                                                        )
                                                                    )
                                                                    scope.launch {
                                                                        bottomSheetScaffoldState.bottomSheetState.partialExpand()
                                                                    }
                                                                    isAnswerVisible = true
                                                                    isAnimationActive.value = true

                                                                    keyboardController?.hide()
                                                                }) {

                                                            val questionText =
                                                                question.questionText[currentLanguageCode]


                                                            Text(
                                                                text = questionText ?: "",
                                                                color = Color.White
                                                            )


                                                        }


                                                    }
                                                }
                                            } else {
                                                Row(
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    modifier = Modifier
                                                        .padding(16.dp)
                                                        .fillMaxWidth()
                                                ) {
                                                    Text(
                                                        text = "No Matching Question.",
                                                        color = Color.White
                                                    )


                                                }
                                            }


                                        }
                                    }
                                }
                            },
                            sheetPeekHeight = 80.dp,
                            sheetContainerColor = PrimaryGreen.copy(alpha = 0.95f)

                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.background(color = LightBeige)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(PrimaryGreen.copy(alpha = 0.3f))
                                        .padding(15.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(modifier = Modifier)

                                    TypingAnimation(
                                        isAnimationActive = isAnimationActive,
                                        transition = typingTransition
                                    )

                                }

                                LazyColumn(
                                    state = scrollState,
                                    modifier = Modifier.padding(bottom = 80.dp)
                                )
                                {

                                    questionSessionWithQuestionAndAnswersListGroupedByDate.forEach { (date, questionSessionList) ->
                                        item {
                                            DateBox(
                                                date = date,
                                                currentLanguageCode = currentLanguageCode ?: "en"
                                            )
                                        }

                                        itemsIndexed(questionSessionList) { index, questionSessionWithQuestionAndAnswer ->

                                            questionSessionWithQuestionAndAnswer?.let { questionSessionAnswerTriple ->
                                                val timeDifference = Duration.between(
                                                    Instant.ofEpochSecond(
                                                        questionSessionAnswerTriple.first.timeAsked.epochSeconds
                                                    ),
                                                    Instant.ofEpochSecond(RealmInstant.now().epochSeconds)
                                                ).seconds

                                                questionSessionAnswerTriple.second?.let { question ->
                                                    QuestionBox(
                                                        question = question,
                                                        questionSession = questionSessionAnswerTriple.first,
                                                        onEvent = onEvent,
                                                        currentLanguageCode = currentLanguageCode
                                                            ?: Language.English.isoCode,
                                                        openAlertDialogState = openAlertDialog
                                                    )
                                                }

                                                questionSessionAnswerTriple.third?.forEachIndexed { answerIndex, answer ->
                                                    // todo check how long ago questions session was loaded
                                                    if ((index == questionSessionWithQuestionAndAnswersList.lastIndex) and (timeDifference < 25) and (answerIndex != 0)) {
                                                        var isVisible by remember {
                                                            mutableStateOf(false)
                                                        }

                                                        // different behavior for first answer
                                                        LaunchedEffect(key1 = isVisible) {
                                                            isAnimationActive.value = true
                                                            delay((answerIndex + 1) * 5000L)
                                                            isVisible = true

                                                            if (questionSessionWithQuestionAndAnswersList.isNotEmpty()) {
                                                                scope.launch {
                                                                    scrollState.animateScrollToItem(
                                                                        questionSessionWithQuestionAndAnswersList.lastIndex + 1,
                                                                        scrollOffset = 360
                                                                    )
                                                                }
                                                            }

                                                            isAnimationActive.value = false


                                                        }

                                                        AnimatedVisibility(visible = isVisible) {
                                                            AnswerBox(
                                                                questionAnswer = answer,
                                                                currentLanguageCode = currentLanguageCode
                                                                    ?: Language.English.isoCode
                                                            )
                                                        }
                                                    } else {
                                                        AnswerBox(
                                                            questionAnswer = answer,
                                                            currentLanguageCode = currentLanguageCode
                                                                ?: Language.English.isoCode
                                                        )
                                                    }


                                                }

                                            }

                                        }


                                    }




                                    item {
                                        Box(
                                            modifier = Modifier
                                                .height(57.dp)
                                                .background(color = LighterBeige)
                                        ) {

                                        }
                                    }

                                    if (questionSessionWithQuestionAndAnswersList.isNotEmpty()) {
                                        scope.launch {
                                            scrollState.animateScrollToItem(
                                                questionSessionWithQuestionAndAnswersList.lastIndex + 1,
                                            )
                                        }
                                    }
                                }
                            }

                        }
                    }
                })

        }
    }

}


