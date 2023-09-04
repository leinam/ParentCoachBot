package com.example.parentcoachbot.feature_chat.presentation

sealed class Screen(val route: String) {
    object SplashScreen: Screen("splash_screen")
    object ExploreOnboardingScreen: Screen("explore_onboarding_screen")
    object SearchOnboardingScreen: Screen("search_onboarding_screen")
    object FavouriteOnboardingScreen: Screen("favourite_onboarding_screen")
    object ChatListScreen: Screen("chat_list")
    object ChatScreen: Screen("chat_screen")
    object CreateProfileSplashScreen: Screen("create_profile_splash_screen")
    object AddProfileScreen: Screen("add_profile_screen")
    object SelectProfileScreen: Screen("select_profile")
}