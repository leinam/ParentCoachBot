package com.example.parentcoachbot.ui.theme

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.parentcoachbot.R
import com.example.parentcoachbot.feature_chat.presentation.Screen

data class NavigationItem(
    @StringRes val title: Int?,
    @DrawableRes val icon: Int,
    val route: String?
)

val drawerItemsList = listOf(
    NavigationItem(
        R.string.switch_profile_drawer_label,
        R.drawable.profile_icon,
        route = Screen.SelectProfileScreen.route
    ),

    NavigationItem(
        R.string.chats_label,
        R.drawable.chats_icon,
        route = Screen.ChatListScreen.route
    ),

    NavigationItem(
        R.string.help_label,
        R.drawable.help_icon,
        route = Screen.EmergencyInfoScreen.route
    ),

    NavigationItem(
        R.string.saved_questions_menu_label,
        R.drawable.favourites_icon,
        route = Screen.SavedQuestionsScreen.route
    ),

    NavigationItem(
        R.string.resources_label,
        R.drawable.resources_icon,
        route = Screen.ResourcesHomeScreen.route
    ),

    NavigationItem(
        R.string.settings_label,
        R.drawable.settings_icon,
        route = Screen.SettingsHomeScreen.route
    )
)

val settingsItemList = listOf(
    NavigationItem(
    R.string.profile_label,
    R.drawable.baseline_account_circle_24,
    route = Screen.UpdateProfileScreen.route
),

    NavigationItem(
        R.string.language_label,
        R.drawable.baseline_language_24,
        route = Screen.SelectLanguageScreen.route
    ),

    NavigationItem(
        R.string.data_privacy_label,
        R.drawable.baseline_security_24,
        route = Screen.SettingsHomeScreen.route
    ),

    NavigationItem(
        R.string.terms_label,
        R.drawable.baseline_insert_drive_file_24,
        route = Screen.SettingsHomeScreen.route
    ),

    )


