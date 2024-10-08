package com.example.parentcoachbot.feature_chat.presentation.chat_list

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.parentcoachbot.R
import com.example.parentcoachbot.feature_chat.domain.model.ChatSession
import com.example.parentcoachbot.feature_chat.domain.model.ChildProfile
import com.example.parentcoachbot.feature_chat.domain.util.Language
import com.example.parentcoachbot.feature_chat.presentation.Screen
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.ChatEvent
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.TopNavBar
import com.example.parentcoachbot.ui.theme.BackgroundBeige
import com.example.parentcoachbot.ui.theme.ChatListGreen
import com.example.parentcoachbot.ui.theme.PlexSans
import com.example.parentcoachbot.ui.theme.PrimaryGreen
import com.example.parentcoachbot.ui.theme.TextGrey
import com.example.parentcoachbot.ui.theme.ThinGreen
import com.example.parentcoachbot.ui.theme.drawerItemsList
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Preview
@Composable
fun ChatListScreen(
    chatListViewModelState: State<ChatListStateWrapper> = mutableStateOf(ChatListStateWrapper()),
    navController: NavController = rememberNavController(),
    onChatListEvent: (ChatListEvent) -> Unit = {},
    onChatEvent: (ChatEvent) -> Unit = {}
) {

    val chatListStateWrapper = chatListViewModelState.value
    val appPreferences: SharedPreferences = LocalContext.current.getSharedPreferences(
        "MyAppPreferences",
        Context.MODE_PRIVATE
    )

    val currentLanguageCode = appPreferences.getString("default_language", Language.English.isoCode)
        ?: Language.English.isoCode

    val chatSessionList: List<ChatSession> by chatListStateWrapper.chatSessionListState.collectAsStateWithLifecycle()
    val currentChildProfile: ChildProfile? by chatListStateWrapper.currentChildProfile.collectAsStateWithLifecycle()


    val scope = rememberCoroutineScope()
    var drawerSelectedItemIndex by rememberSaveable { mutableIntStateOf(1) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)



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
    ) {
        Scaffold(
            topBar = {
                TopNavBar(
                    navController = navController,
                    screenIndex = 0,
                    drawerState = drawerState,
                    scope = scope
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        onChatListEvent(ChatListEvent.NewChat).also {
                            navController.navigate(Screen.ChatScreen.route)
                        }
                    },
                    containerColor = PrimaryGreen,
                    contentColor = Color.White
                )
                {
                    Icon(
                        painter = painterResource(
                            id = R.drawable.newchat_icon
                        ),
                        contentDescription = stringResource(R.string.new_chat_label)
                    )
                }
            }
        )
        { contentPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = BackgroundBeige)
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
                            text = stringResource(id = R.string.chats_label),
                            color = PrimaryGreen,
                            fontFamily = PlexSans,
                            fontWeight = Normal,
                            fontSize = 19.sp
                        )

                        Icon(
                            painter = painterResource(id = R.drawable.baseline_more_vert_24),
                            contentDescription = null, tint = PrimaryGreen
                        )
                    }

                    if (chatSessionList.isNotEmpty()) {
                        LazyColumn {
                            items(chatSessionList) { chatSession ->
                                var isContextMenuVisible by rememberSaveable {
                                    mutableStateOf(false)
                                }

                                Box(modifier = Modifier.onSizeChanged { }) {
                                    Box(modifier = Modifier
                                        .padding(10.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(color = ChatListGreen)
                                        .fillMaxWidth()
                                        .height(90.dp)
                                        .padding(15.dp)
                                        .pointerInput(true) {
                                            detectTapGestures(onLongPress = {
                                                isContextMenuVisible = true
                                            }, onTap = {
                                                onChatEvent(ChatEvent.SelectChat(chatSession))
                                                navController.navigate(route = Screen.ChatScreen.route)
                                            })
                                        }

                                    )
                                    {

                                        val titleText: String =
                                            chatSession.chatTitle[currentLanguageCode]
                                                ?: stringResource(id = R.string.new_chat_label)
                                        Text(
                                            text = titleText.substring(
                                                startIndex = 0,
                                                endIndex = if (titleText.length < 30) titleText.length else 29
                                            ),
                                            fontSize = 16.sp,
                                            color = TextGrey,
                                            fontFamily = PlexSans,
                                            fontWeight = SemiBold,
                                            modifier = Modifier.align(
                                                Alignment.TopStart
                                            )
                                        )

                                        var lastUpdated = ""

                                        chatSession.timeLastUpdated?.let {
                                            lastUpdated = LocalDateTime.ofEpochSecond(
                                                it.epochSeconds,
                                                it.nanosecondsOfSecond,
                                                ZoneOffset.UTC
                                            ).format(DateTimeFormatter.ofPattern("E H:mm"))
                                        }

                                        Row(
                                            modifier = Modifier
                                                .align(Alignment.TopEnd),
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {

                                            if (chatSession.isPinned) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.baseline_push_pin_24),
                                                    contentDescription = null,
                                                    modifier = Modifier
                                                        .size(15.dp)
                                                        .padding(end = 5.dp),
                                                    tint = TextGrey
                                                )
                                            }

                                            Text(
                                                text = lastUpdated,
                                                fontSize = 10.sp,
                                                fontFamily = PlexSans,
                                                fontWeight = SemiBold,

                                                )
                                        }

                                        Row(
                                            modifier = Modifier
                                                .align(Alignment.BottomStart)
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            chatSession.lastAnswerText[currentLanguageCode]?.let {
                                                if (it.isNotBlank()) {
                                                    Text(
                                                        text = "ParentCoach: ${
                                                            it.substring(
                                                                startIndex = 0,
                                                                endIndex = if (it.length < 34) it.length - 1 else 33
                                                            )
                                                        }",
                                                        fontSize = 12.sp,
                                                        fontFamily = PlexSans,
                                                        fontWeight = Normal
                                                    )

                                                    Icon(
                                                        painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                                                        contentDescription = null, tint = TextGrey
                                                    )

                                                }


                                            }


                                        }


                                    }
                                    DropdownMenu(
                                        expanded = isContextMenuVisible,
                                        onDismissRequest = { isContextMenuVisible = false },
                                        modifier = Modifier.padding(10.dp)
                                    ) {
                                        DropdownMenuItem(
                                            text = { Text(text = stringResource(id = R.string.delete_chat_label)) },
                                            onClick = {
                                                onChatListEvent(
                                                    ChatListEvent.DeleteChat(
                                                        chatSession
                                                    )
                                                )

                                                isContextMenuVisible = false
                                            })

                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = if (chatSession.isPinned) stringResource(
                                                        id = R.string.unpin_chat_label
                                                    ) else stringResource(
                                                        id = R.string.pin_chat_label
                                                    )
                                                )
                                            },
                                            onClick = {
                                                onChatListEvent(
                                                    ChatListEvent.PinChat(
                                                        chatSession
                                                    )
                                                )

                                                isContextMenuVisible = false
                                            })

                                    }
                                }
                            }
                        }
                    } else {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Box(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(color = ThinGreen)
                                    .padding(15.dp)
                                    .align(Alignment.Center)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.no_chats_label).uppercase(),
                                    textAlign = TextAlign.Center, color = TextGrey
                                )
                            }
                        }
                    }

                }
            }
        }
    }
}





