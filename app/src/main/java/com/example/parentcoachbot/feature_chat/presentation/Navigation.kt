package com.example.parentcoachbot.feature_chat.presentation

import ResourcesHomeScreen
import SavedQuestionsScreen
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.parentcoachbot.feature_chat.presentation.chat_list.ChatListEvent
import com.example.parentcoachbot.feature_chat.presentation.chat_list.ChatListScreen
import com.example.parentcoachbot.feature_chat.presentation.chat_list.ChatListViewModel
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.ChatScreen
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.ChatViewModel
import com.example.parentcoachbot.feature_chat.presentation.emergency_screen.EmergencyInfoScreen
import com.example.parentcoachbot.feature_chat.presentation.profile_screen.AddProfileScreen
import com.example.parentcoachbot.feature_chat.presentation.profile_screen.CreateProfileSplashScreen
import com.example.parentcoachbot.feature_chat.presentation.profile_screen.ProfileViewModel
import com.example.parentcoachbot.feature_chat.presentation.profile_screen.SelectProfileScreen
import com.example.parentcoachbot.feature_chat.presentation.profile_screen.UpdateProfileScreen
import com.example.parentcoachbot.feature_chat.presentation.settings_screen.SelectLanguageScreen
import com.example.parentcoachbot.feature_chat.presentation.settings_screen.SettingsHomeScreen
import com.example.parentcoachbot.ui.theme.OnboardingPageItem

@Composable
fun Navigation() {
    val navHostController: NavHostController = rememberNavController()
    val chatListViewModel = hiltViewModel<ChatListViewModel>()
    val chatViewModel = hiltViewModel<ChatViewModel>()
    val profileViewModel: ProfileViewModel = hiltViewModel()


    NavHost(
        navController = navHostController,
        startDestination = Screen.FirstTimeSplashScreen.route
    ) {
        composable(route = Screen.FirstTimeSplashScreen.route) {
            FirstTimeSplashScreen(navController = navHostController,
                chatViewModelState = chatViewModel.chatViewModelState,
                onEvent = { chatEvent -> chatViewModel.onEvent(chatEvent) })
        }

        composable(route = Screen.SelectProfileScreen.route) {
            SelectProfileScreen(navController = navHostController,
                profileState = profileViewModel.profileViewModelState,
                onEvent = { profileEvent ->
                    profileViewModel.onEvent(profileEvent)
                    // todo is it really necessary to trigger this - can we observe and trigger directly
                    chatListViewModel.onEvent(chatListEvent = ChatListEvent.SelectProfile)

                })
        }

        composable(route = Screen.ExploreOnboardingScreen.route) {
            OnboardingScreen(
                onboardingPageItem = OnboardingPageItem.ExploreTopics,
                navController = navHostController
            )
        }

        composable(route = Screen.SearchOnboardingScreen.route) {
            OnboardingScreen(
                onboardingPageItem = OnboardingPageItem.SearchQuestions,
                navController = navHostController
            )
        }

        composable(route = Screen.FavouriteOnboardingScreen.route) {
            OnboardingScreen(
                onboardingPageItem = OnboardingPageItem.SaveFavourites,
                navController = navHostController
            )
        }

        composable(route = Screen.ChatListScreen.route) {

            ChatListScreen(navController = navHostController,
                chatListViewModelState = chatListViewModel.chatListViewModelState,
                onChatListEvent = { chatListEvent -> chatListViewModel.onEvent(chatListEvent) },
                onChatEvent = { chatEvent -> chatViewModel.onEvent(chatEvent) }
            )
        }

        composable(route = Screen.ChatScreen.route) {

            ChatScreen(chatViewModelState = chatViewModel.chatViewModelState,
                navController = navHostController,
                onEvent = { chatEvent -> chatViewModel.onEvent(chatEvent) })
        }

        composable(route = Screen.CreateProfileSplashScreen.route) {
            CreateProfileSplashScreen(navController = navHostController)
        }

        composable(route = Screen.UpdateProfileScreen.route) {
            UpdateProfileScreen(navController = navHostController)
        }

        composable(route = Screen.AddProfileScreen.route) {
            AddProfileScreen(navController = navHostController,
                profileState = profileViewModel.profileViewModelState,
                onEvent = { profileEvent ->
                    profileViewModel.onEvent(profileEvent)
                    chatListViewModel.onEvent(chatListEvent = ChatListEvent.SelectProfile)
                })
        }
        composable(route = Screen.SettingsHomeScreen.route) {
            SettingsHomeScreen(navController = navHostController)
        }
        composable(route = Screen.SelectLanguageScreen.route) {
            SelectLanguageScreen(navController = navHostController,
                chatViewModelState = chatViewModel.chatViewModelState,
                onEvent = { chatEvent -> chatViewModel.onEvent(chatEvent) })
        }
        composable(route = Screen.SavedQuestionsScreen.route) {
            SavedQuestionsScreen(navController = navHostController)
        }
        composable(route = Screen.ResourcesHomeScreen.route) {
            ResourcesHomeScreen(navController = navHostController)
        }
        composable(route = Screen.EmergencyInfoScreen.route) {
            EmergencyInfoScreen(navController = navHostController)
        }

    }
}