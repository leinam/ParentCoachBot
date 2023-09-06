package com.example.parentcoachbot.feature_chat.presentation.chat_list

import androidx.annotation.DrawableRes
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.Density
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
import com.example.parentcoachbot.ui.theme.DarkGrey
import com.example.parentcoachbot.ui.theme.LightBeige
import com.example.parentcoachbot.ui.theme.NavBarItem
import com.example.parentcoachbot.ui.theme.PlexSans
import com.example.parentcoachbot.ui.theme.PrimaryGreen
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


@Composable
fun ChatListScreen(chatListViewModelState: State<ChatListStateWrapper>,
                   navController: NavController = rememberNavController(),
                   onChatListEvent:(ChatListEvent) -> Unit,
                   onChatEvent:(ChatEvent) -> Unit) {

    val chatListStateWrapper = chatListViewModelState.value

    val chatSessionList: List<ChatSession> by chatListStateWrapper.chatSessionListState.collectAsStateWithLifecycle()
    val newChatSession: ChatSession? by chatListStateWrapper.newChatState.collectAsStateWithLifecycle()

    val drawerItemList: List<NavBarItem> = listOf(
        NavBarItem("Profile",
            R.drawable.profile_icon,
            route = Screen.SelectProfileScreen.route),

        NavBarItem("Chats",
            R.drawable.chats_icon,
            route = Screen.ChatListScreen.route),

        NavBarItem("Help",
            R.drawable.help_icon,
            route = "help"),

        NavBarItem("Saved",
            R.drawable.favourites_icon,
            route = "saved"),

        NavBarItem("Resources",
            R.drawable.resources_icon,
            route = "resources"),

        NavBarItem("Settings",
            R.drawable.settings_icon,
            route = "settings"),
    )

    val scope = rememberCoroutineScope()
    var drawerSelectedItemIndex by rememberSaveable { mutableIntStateOf(3) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    // offset drop down menu to appear depending on where

    var itemHeight by remember {
        mutableStateOf(0.dp)
    }

    val density:Density = LocalDensity.current
    LocalHapticFeedback.current


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet()
            {
                drawerItemList.forEachIndexed { index, navBarItem ->
                    NavigationDrawerItem(
                        label = {
                            navBarItem.title?.let { Text(text = it) }
                                },
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
                        onChatListEvent(ChatListEvent.NewChat).apply {
                            newChatSession?.let {
                                onChatEvent(ChatEvent.SelectChat(it))
                            }
                            navController.navigate(Screen.ChatScreen.route)
                        }

                    },
                    containerColor = PrimaryGreen,
                    contentColor = Color.White)
                {
                    Icon(painter = painterResource(
                        id = R.drawable.newchat_icon),
                        contentDescription = "new chat")
                }
            }
        )
        {contentPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = LightBeige)
                    .padding(contentPadding)

            ){

                Column {

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .background(PrimaryGreen.copy(alpha = 0.3f))
                        .padding(17.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically){

                        Text(text = "Chats",
                            color = PrimaryGreen,
                            fontFamily = PlexSans,
                            fontWeight = Normal,
                            fontSize = 19.sp)

                        Icon(painter = painterResource(id = R.drawable.baseline_more_vert_24),
                            contentDescription = "More Options", tint = PrimaryGreen)
                    }

                    LazyColumn {
                        items(chatSessionList){
                                chatSession ->
                            Box(modifier = Modifier
                                .padding(10.dp)
                                .height(90.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .fillMaxWidth()
                                .background(PrimaryGreen.copy(alpha = 0.3f))
                                .padding(10.dp)
                                .clickable {
                                    onChatEvent(ChatEvent.SelectChat(chatSession))
                                    // onChatListEvent(ChatListEvent.SelectChat(chatSession))
                                    navController.navigate(route = Screen.ChatScreen.route)
                                }
                                // true reflects state of whether to detect
                                )
                            {

                                Text(text = chatSession.chatTitle, fontSize = 16.sp,
                                    fontFamily = PlexSans, fontWeight = SemiBold,
                                    modifier = Modifier.align(
                                        Alignment.TopStart))

                                var lastUpdated = ""

                                chatSession.timeLastUpdated?.let {
                                    lastUpdated = LocalDateTime.ofEpochSecond(it.epochSeconds, it.nanosecondsOfSecond, ZoneOffset.UTC
                                        ).format(DateTimeFormatter.ofPattern("E H:m"))
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
                                        contentDescription = "More", tint = DarkGrey)
                                }

                                }

                            }
                        }
                    }

                }
            }
        }
    }


data class ChatListDropdownItem(val itemTitle: String, @DrawableRes val itemIcon: Int)

val chatListDropdownItemList = listOf(
    ChatListDropdownItem("Delete Chat",
        itemIcon = R.drawable.baseline_delete_24),
    ChatListDropdownItem("Pin Chat",
        itemIcon = R.drawable.baseline_push_pin_24)
)

