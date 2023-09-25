package com.example.parentcoachbot.ui.theme

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.parentcoachbot.R
import com.example.parentcoachbot.feature_chat.presentation.Screen

data class NavBarItem(
    @StringRes val title: Int?,
    @DrawableRes val icon: Int,
    val route: String?
)

val drawerItemsList = listOf(
    NavBarItem(
        R.string.switch_profile_drawer_label,
        R.drawable.profile_icon,
        route = Screen.SelectProfileScreen.route
    ),

    NavBarItem(
        R.string.chats_label,
        R.drawable.chats_icon,
        route = Screen.ChatListScreen.route
    ),

    NavBarItem(
        R.string.help_label,
        R.drawable.help_icon,
        route = Screen.EmergencyInfoScreen.route
    ),

    NavBarItem(
        R.string.saved_questions_menu_label,
        R.drawable.favourites_icon,
        route = Screen.SavedQuestionsScreen.route
    ),

    NavBarItem(
        R.string.resources_label,
        R.drawable.resources_icon,
        route = Screen.ResourcesHomeScreen.route
    ),

    NavBarItem(
        R.string.settings_label,
        R.drawable.settings_icon,
        route = Screen.SettingsHomeScreen.route
    ),
)


