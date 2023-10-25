package com.example.parentcoachbot.feature_chat.presentation

import ResourcesHomeScreen
import SavedQuestionsScreen
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
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
import com.google.firebase.analytics.FirebaseAnalytics

@Composable
fun Navigation() {
    val navHostController: NavHostController = rememberNavController()
    val chatListViewModel = hiltViewModel<ChatListViewModel>()
    val chatViewModel = hiltViewModel<ChatViewModel>()
    val profileViewModel: ProfileViewModel = hiltViewModel()

    val firebaseAnalytics: FirebaseAnalytics = chatViewModel.firebaseAnalytics
    firebaseAnalytics.setUserId("Leina")

    DisposableEffect(Unit) {
        var screenEntryTime: Long = 0L

        val listener = NavController.OnDestinationChangedListener { controller, destination, _ ->
            val entryParams = Bundle()
            val destinationRoute = destination.route
            val previousScreenRoute = controller.previousBackStackEntry?.destination?.route
            val destinationChangeTime = System.currentTimeMillis()

            previousScreenRoute?.let {
                val exitParams = Bundle()
                val exitTime: Long = destinationChangeTime

                val timeSpentOnScreen =
                    if (screenEntryTime != 0L) screenEntryTime - exitTime else 0L
                exitParams.putString("SCREEN_NAME", previousScreenRoute)
                exitParams.putLong("TIME_ON_SCREEN", timeSpentOnScreen)

                firebaseAnalytics.logEvent("SCREEN_EXIT", exitParams)
            }


            destinationRoute?.let {
                screenEntryTime = System.currentTimeMillis()
                entryParams.putString(FirebaseAnalytics.Param.SCREEN_NAME, destinationRoute)
                entryParams.putString("PREVIOUS_SCREEN", previousScreenRoute ?: "null")

                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, entryParams)
            }

        }

        navHostController.addOnDestinationChangedListener(listener)

        // Dispose of the listener when the composable is no longer in the composition
        onDispose {

            firebaseAnalytics.logEvent("EXITED_FROM", Bundle().apply {
                this.putString(
                    "EXITED_FROM",
                    navHostController.currentBackStackEntry?.destination?.route
                )
            })

            navHostController.removeOnDestinationChangedListener(listener)
        }
    }

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