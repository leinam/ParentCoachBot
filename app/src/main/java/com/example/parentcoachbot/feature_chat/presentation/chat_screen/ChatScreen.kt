package com.example.parentcoachbot.feature_chat.presentation.chat_screen

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.AnswerBox
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.QuestionBox
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.QuestionInputSection
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.TopNavBar
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.TypingAnimation
import com.example.parentcoachbot.ui.theme.BackgroundWhite
import com.example.parentcoachbot.ui.theme.LightBeige
import com.example.parentcoachbot.ui.theme.LightGreen
import com.example.parentcoachbot.ui.theme.ParentCoachBotTheme
import com.example.parentcoachbot.ui.theme.PrimaryGreen
import com.example.parentcoachbot.ui.theme.drawerItemsList
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant

// todo modularize chat screen composable
@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    ChatViewModelState: State<ChatStateWrapper> = mutableStateOf(ChatStateWrapper()),
    navController: NavController = rememberNavController(),
    onEvent: (chatEvent: ChatEvent) -> Unit = {}
) {

    val chatStateWrapper = ChatViewModelState.value

    val topicsList: List<Topic> by chatStateWrapper.topicsListState.collectAsStateWithLifecycle()
    val questionSessionWithQuestionAndAnswersList by chatStateWrapper.questionSessionsWithQuestionAndAnswersState.collectAsStateWithLifecycle()
    val subtopicList: List<Subtopic> by chatStateWrapper.subtopicsListState.collectAsStateWithLifecycle()
    val subtopicQuestionsList: List<Question> by chatStateWrapper.subtopicQuestionsListState.collectAsStateWithLifecycle()
    val searchResultQuestionsList: List<Question> by chatStateWrapper.searchResultsQuestionsListState.collectAsStateWithLifecycle()
    val currentChildProfile: ChildProfile? by chatStateWrapper.currentChildProfile.collectAsStateWithLifecycle()
    var isAnswerVisible by remember { mutableStateOf(false) }


    val scope = rememberCoroutineScope()
    var drawerSelectedItemIndex by rememberSaveable {
        mutableIntStateOf(1)
    }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scrollState = rememberLazyListState()
    var isAnimationActive = remember {
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

    val currentTopic: Topic? by chatStateWrapper.currentTopicState.collectAsStateWithLifecycle()
    var currentSubtopic: Subtopic? by remember { mutableStateOf(null) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet()
            {
                drawerItemsList.forEachIndexed { index, navBarItem ->
                    NavigationDrawerItem(
                        label = {
                            if (index == 0) navBarItem.title?.let {
                                Text(
                                    text = stringResource(
                                        id = it
                                    ) + ": ${currentChildProfile?.name}"
                                )
                            } else navBarItem.title?.let { Text(text = stringResource(id = it)) }
                        },
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
    )
    {
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
                Box(
                    modifier = Modifier
                        .background(color = BackgroundWhite)
                        .fillMaxSize()
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

                                            Text(
                                                text = "${currentSubtopic?.title?.uppercase()}",
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

                                                    question.questionTextEn?.let {
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
                                                tint = BackgroundWhite,
                                                modifier = Modifier
                                                    .clickable {
                                                        bottomSheetContentState.value =
                                                            BottomSheetContent.Topics
                                                    }
                                                    .align(Alignment.CenterStart)
                                                    .padding(horizontal = 10.dp))

                                            Text(
                                                text = "${currentTopic?.title?.uppercase()} " + stringResource(
                                                    id = R.string.topics_label
                                                ).uppercase(),
                                                color = LightBeige, modifier =
                                                Modifier.align(Alignment.Center)
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

                                                    subtopic.title?.let {
                                                        Text(
                                                            text = it,
                                                            color = Color.White
                                                        )
                                                    }

                                                    subtopic.icon?.let {
                                                        Icon(
                                                            painter = painterResource(id = subtopic.icon!!),
                                                            contentDescription = subtopic.title,
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
                                                    topic.title?.let {
                                                        Text(
                                                            text = it,
                                                            color = Color.White
                                                        )
                                                    }

                                                    topic.icon?.let {
                                                        Icon(
                                                            painter = painterResource(id = it),
                                                            contentDescription = topic.title,
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

                                                        question.questionTextEn?.let {
                                                            Text(
                                                                text = it,
                                                                color = Color.White
                                                            )
                                                        }

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
                        sheetContainerColor = PrimaryGreen.copy(alpha = 0.98f)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(PrimaryGreen.copy(alpha = 0.3f))
                                    .padding(15.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(modifier = Modifier)

                                TypingAnimation(isAnimationActive = isAnimationActive)

                            }

                            LazyColumn(
                                state = scrollState,
                                modifier = Modifier.padding(bottom = 80.dp)
                            )
                            {


                                itemsIndexed(questionSessionWithQuestionAndAnswersList) { index, questionSessionWithQuestionAndAnswer ->

                                    questionSessionWithQuestionAndAnswer?.let { questionSessionAnswerTriple ->
                                        val timeDifference = Duration.between(
                                            Instant.ofEpochSecond(questionSessionAnswerTriple.first.timeAsked.epochSeconds),
                                            Instant.ofEpochSecond(RealmInstant.now().epochSeconds)
                                        ).seconds

                                        questionSessionAnswerTriple.second?.let { question ->
                                            QuestionBox(question = question)
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
                                                    delay((answerIndex + 1) * 3500L)
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
                                                    AnswerBox(questionAnswer = answer)
                                                }
                                            } else {
                                                AnswerBox(questionAnswer = answer)
                                            }


                                        }

                                    }

                                }


                                item {
                                    Box(modifier = Modifier.height(57.dp)) {

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
            }
        }

    }
}

