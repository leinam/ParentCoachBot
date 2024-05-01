package com.example.parentcoachbot.feature_chat.presentation

sealed class Screen(val route: String) {
    object FirstTimeSplashScreen: Screen("first_time_screen")
    object ExploreOnboardingScreen: Screen("explore_onboarding_screen")
    object SearchOnboardingScreen: Screen("search_onboarding_screen")
    object FavouriteOnboardingScreen: Screen("favourite_onboarding_screen")
    object ChatListScreen: Screen("chat_list")
    object ChatScreen: Screen("chat_screen")
    object CreateProfileSplashScreen: Screen("create_profile_splash_screen")
    object AddProfileScreen: Screen("add_profile_screen")
    object AccountSetupScreen: Screen("account_setup_screen")
    object PinEntryScreen: Screen("pin_entry_screen")
    object UpdateProfileScreen: Screen("update_profile_screen")
    object SelectProfileScreen: Screen("select_profile")
    object SelectLanguageScreen: Screen("select_language")
    object SettingsHomeScreen: Screen("settings_screen")
    object ResourcesHomeScreen: Screen("resources_screen")
    object PDFResourceScreen: Screen("pdf_resource_screen")

    object ImageResourceScreen: Screen("image_resource_screen")
    object SavedQuestionsScreen: Screen("saved_questions_screen")
    object EmergencyInfoScreen: Screen("emergency_info_screen")
    object ChangeCountryScreen: Screen("change_country_screen")
    object DataPrivacyScreen: Screen("data_privacy_screen")
    object TermsOfUseScreen: Screen("terms_of_use_screen")
}