package com.example.parentcoachbot.ui.theme

import androidx.annotation.DrawableRes
import com.example.parentcoachbot.R
import com.example.parentcoachbot.feature_chat.presentation.Screen

data class NavBarItem(
    val title: String?,
    @DrawableRes val icon: Int,
    val route: String?
)

val drawerItemsList = listOf(
    NavBarItem(
        "Switch Profile",
        R.drawable.profile_icon,
        route = Screen.SelectProfileScreen.route
    ),

    NavBarItem(
        "Chats",
        R.drawable.chats_icon,
        route = Screen.ChatListScreen.route
    ),

    NavBarItem(
        "Help",
        R.drawable.help_icon,
        route = Screen.EmergencyInfoScreen.route
    ),

    NavBarItem(
        "Saved",
        R.drawable.favourites_icon,
        route = Screen.SavedQuestionsScreen.route
    ),

    NavBarItem(
        "Resources",
        R.drawable.resources_icon,
        route = Screen.ResourcesHomeScreen.route
    ),

    NavBarItem(
        "Settings",
        R.drawable.settings_icon,
        route = Screen.SettingsHomeScreen.route
    ),
)


