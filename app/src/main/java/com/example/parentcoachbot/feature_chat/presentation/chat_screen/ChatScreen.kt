package com.example.parentcoachbot.feature_chat.presentation.chat_screen

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
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.parentcoachbot.R
import com.example.parentcoachbot.feature_chat.domain.model.ChildProfile
import com.example.parentcoachbot.feature_chat.domain.model.Subtopic
import com.example.parentcoachbot.feature_chat.domain.model.Topic
import com.example.parentcoachbot.feature_chat.presentation.Screen
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.AnswerBox
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.QuestionBox
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.QuestionInputSection
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.TopNavBar
import com.example.parentcoachbot.ui.theme.BackgroundWhite
import com.example.parentcoachbot.ui.theme.LightBeige
import com.example.parentcoachbot.ui.theme.LightGreen
import com.example.parentcoachbot.ui.theme.NavBarItem
import com.example.parentcoachbot.ui.theme.ParentCoachBotTheme
import com.example.parentcoachbot.ui.theme.PrimaryGreen
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(ChatViewModelState: State<ChatStateWrapper> = mutableStateOf(ChatStateWrapper()),
               navController: NavController = rememberNavController()
) {
    val chatStateWrapper = ChatViewModelState.value

    val topicsList:List<Topic> by chatStateWrapper.topicsListState.collectAsStateWithLifecycle()
    val questionsWithAnswersList by chatStateWrapper.questionsWithAnswersState.collectAsStateWithLifecycle()
    val questionSessionList by chatStateWrapper.questionSessionListState.collectAsState()
    val subtopicList: List<Subtopic> by chatStateWrapper.subtopicsListState.collectAsStateWithLifecycle()
    val childProfileList: List<ChildProfile> by chatStateWrapper.childProfilesListState.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val drawerItemsList = listOf(
        NavBarItem("Profile",
            R.drawable.profile_icon,
            route = Screen.ChatScreen.route),

        NavBarItem("Chats",
            R.drawable.chats_icon,
            route = Screen.ChatListScreen.route),

        NavBarItem("Help",
            R.drawable.help_icon,
            route = null),

        NavBarItem("Saved",
            R.drawable.favourites_icon,
            route = null),

        NavBarItem("Resources",
            R.drawable.resources_icon,
            route = null),

        NavBarItem("Settings",
            R.drawable.settings_icon,
            route = null),
    )

    var drawerSelectedItemIndex by rememberSaveable {
        mutableIntStateOf(1)
    }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var bottomSheetContent: BottomSheetContent by remember { mutableStateOf(BottomSheetContent.Topics) }
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded, skipHiddenState = false)
    )
    var currentTopic: Topic? by remember { mutableStateOf(null) }
    var currentSubtopic: Subtopic? by remember { mutableStateOf(null) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet()
            {

                drawerItemsList.forEachIndexed { index, navBarItem ->
                    NavigationDrawerItem(
                        label = { navBarItem.title?.let { Text(text = it) } },
                        selected = index == drawerSelectedItemIndex,
                        onClick = {
                            drawerSelectedItemIndex = index
                            scope.launch{
                                drawerState.close()
                                navBarItem.route?.let { navController.navigate(route = it) }
                            }

                        },
                        icon = {
                            Icon(painter = painterResource(id = navBarItem.icon),
                                contentDescription = navBarItem.title)
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
                            .padding(10.dp))
                },
                topBar = {
                    TopNavBar(
                        drawerState = drawerState,
                        scope = scope,
                        navController = navController,
                        onProfileSelectionEvent = {
                            bottomSheetContent = it
                            scope.launch { bottomSheetScaffoldState.bottomSheetState.expand() }

                        })
                })
            {contentPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = BackgroundWhite)
                        .padding(contentPadding)
                ){

                    BottomSheetScaffold(
                        scaffoldState = bottomSheetScaffoldState,
                        sheetContent = {
                            when (bottomSheetContent){
                                BottomSheetContent.Questions -> {}
                                BottomSheetContent.SubTopics -> {
                                    Column(modifier = Modifier.height(300.dp)){
                                        Box(modifier = Modifier.fillMaxWidth(),
                                            contentAlignment = Alignment.Center){
                                            Text(text = "${currentTopic?.title?.uppercase()} SUBTOPICS",
                                                color = LightBeige
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
                                                            currentSubtopic = subtopic
                                                            bottomSheetContent =
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
                                    Column(modifier = Modifier.height(300.dp)){
                                        Box(modifier = Modifier.fillMaxWidth(),
                                            contentAlignment = Alignment.Center){
                                            Text(text = "TOPICS",
                                                color = LightBeige)
                                        }

                                        Spacer(modifier = Modifier.size(10.dp))

                                        LazyColumn {
                                            items(topicsList) { topic ->
                                                Row(horizontalArrangement = Arrangement.SpaceBetween,
                                                    modifier = Modifier
                                                        .padding(16.dp)
                                                        .fillMaxWidth()
                                                        .clickable {
                                                            currentTopic = topic
                                                            bottomSheetContent =
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
                                    }}
                                BottomSheetContent.ChildProfiles -> {
                                    Column(modifier = Modifier.height(300.dp)){
                                        Box(modifier = Modifier.fillMaxWidth(),
                                            contentAlignment = Alignment.Center){
                                            Text(text = "SWITCH PROFILE",
                                                color = LightBeige)
                                        }

                                        Spacer(modifier = Modifier.size(10.dp))

                                        LazyColumn {
                                            items(childProfileList) { childProfile ->
                                                Row(horizontalArrangement = Arrangement.SpaceBetween,
                                                    modifier = Modifier
                                                        .padding(16.dp)
                                                        .fillMaxWidth()
                                                        .clickable {

                                                        }
                                                ) {
                                                    childProfile.name?.let {
                                                        Text(
                                                            text = it,
                                                            color = Color.White
                                                        )
                                                    }


                                                    Icon(
                                                        painter = painterResource(id = R.drawable.breastfeeding_icon),
                                                        contentDescription = null,
                                                        tint = Color.White
                                                    )


                                                }
                                            }
                                        }
                                    }}
                            }

                        },
                        sheetPeekHeight = 80.dp,
                        sheetContainerColor = PrimaryGreen.copy(alpha = 0.98f)
                    ) {
                        Text(text = "$questionSessionList")
                        LazyColumn {
                            items(questionsWithAnswersList) { questionWithAnswer ->
                                questionWithAnswer?.let {
                                        questionAnswerPair ->
                                    QuestionBox(question = questionAnswerPair.first)

                                    Column {
                                        questionAnswerPair.second.forEach {answer ->
                                            AnswerBox(questionAnswer = answer)
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

}