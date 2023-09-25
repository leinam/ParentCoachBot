package com.example.parentcoachbot.feature_chat.presentation.chat_list

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.parentcoachbot.R
import com.example.parentcoachbot.feature_chat.domain.model.ChatSession
import com.example.parentcoachbot.feature_chat.presentation.Screen
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.ChatEvent
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.TopNavBar
import com.example.parentcoachbot.ui.theme.BackgroundWhite
import com.example.parentcoachbot.ui.theme.DarkGrey
import com.example.parentcoachbot.ui.theme.PlexSans
import com.example.parentcoachbot.ui.theme.PrimaryGreen
import com.example.parentcoachbot.ui.theme.drawerItemsList
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Preview
@Composable
fun ChatListScreen(chatListViewModelState: State<ChatListStateWrapper> = mutableStateOf(ChatListStateWrapper()),
                   navController: NavController = rememberNavController(),
                   onChatListEvent:(ChatListEvent) -> Unit = {},
                   onChatEvent:(ChatEvent) -> Unit = {}) {

    val chatListStateWrapper = chatListViewModelState.value

    val chatSessionList: List<ChatSession> by chatListStateWrapper.chatSessionListState.collectAsStateWithLifecycle()
    val newChatSession: ChatSession? by chatListStateWrapper.newChatState.collectAsStateWithLifecycle()

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
                        label = { navBarItem.title?.let { Text(text = stringResource(id = it)) } },
                        selected = index == drawerSelectedItemIndex,
                        onClick = {
                            drawerSelectedItemIndex = index
                            scope.launch{
                                drawerState.close()
                                navBarItem.route?.let { navController.navigate(route = it) }
                            }
                        },
                        icon = { Icon(painter = painterResource(id = navBarItem.icon),
                            contentDescription = navBarItem.title?.let { stringResource(it) }
                        ) },
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }
    ){
        Scaffold(
            topBar = {
                TopNavBar(
                    navController=navController,
                    screenIndex = 0,
                    drawerState = drawerState,
                    scope = scope)
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        onChatListEvent(ChatListEvent.NewChat).also {
                                navController.navigate(Screen.ChatScreen.route)
                            }
                    },
                    containerColor = PrimaryGreen,
                    contentColor = Color.White)
                {
                    Icon(painter = painterResource(
                        id = R.drawable.newchat_icon),
                        contentDescription = stringResource(R.string.new_chat_label)
                    )
                }
            }
        )
        {contentPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = BackgroundWhite)
                    .padding(contentPadding)

            ){

                Column {

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .background(PrimaryGreen.copy(alpha = 0.3f))
                        .padding(17.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically){

                        Text(text = stringResource(id = R.string.chats_label),
                            color = PrimaryGreen,
                            fontFamily = PlexSans,
                            fontWeight = Normal,
                            fontSize = 19.sp)

                        Icon(painter = painterResource(id = R.drawable.baseline_more_vert_24),
                            contentDescription = null, tint = PrimaryGreen)
                    }

                    LazyColumn {
                        items(chatSessionList){
                                chatSession ->
                            Box(modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth()
                                .height(90.dp)
                                .padding(10.dp)
                                .clickable {
                                    onChatEvent(ChatEvent.SelectChat(chatSession))
                                    navController.navigate(route = Screen.ChatScreen.route)
                                }
                                )
                            {


                                    Text(text = chatSession.chatTitle ?: stringResource(id = R.string.new_chat_label), fontSize = 16.sp,
                                        fontFamily = PlexSans, fontWeight = SemiBold,
                                        modifier = Modifier.align(
                                            Alignment.TopStart))

                                var lastUpdated = ""

                                chatSession.timeLastUpdated?.let {
                                    lastUpdated = LocalDateTime.ofEpochSecond(it.epochSeconds, it.nanosecondsOfSecond, ZoneOffset.UTC
                                        ).format(DateTimeFormatter.ofPattern("E H:mm"))
                                }

                                    Text(text = lastUpdated,
                                    fontSize = 10.sp,
                                    fontFamily = PlexSans, fontWeight = SemiBold,
                                    modifier = Modifier.align(
                                        Alignment.TopEnd))

                                Row(modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(text = "Aurora: Welcome! You can explore all the ", fontSize = 12.sp,
                                        fontFamily = PlexSans, fontWeight = Normal)

                                    Icon(painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                                        contentDescription = null, tint = DarkGrey)
                                }



                                }
                            Divider(color = DarkGrey, modifier = Modifier.padding(horizontal = 10.dp))
                            }
                        }
                    }

                }
            }
        }
    }


data class ChatListDropdownItem(@StringRes val itemTitle: Int, @DrawableRes val itemIcon: Int)

val chatListDropdownItemList = listOf(
    ChatListDropdownItem(R.string.delete_chat_label,
        itemIcon = R.drawable.baseline_delete_24),
    ChatListDropdownItem(R.string.pin_chat_label,
        itemIcon = R.drawable.baseline_push_pin_24)
)

