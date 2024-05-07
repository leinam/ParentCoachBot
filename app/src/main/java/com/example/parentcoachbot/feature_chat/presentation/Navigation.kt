package com.example.parentcoachbot.feature_chat.presentation

import SavedQuestionsScreen
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.example.parentcoachbot.feature_chat.presentation.contact_screen.ContactScreenViewModel
import com.example.parentcoachbot.feature_chat.presentation.contact_screen.EmergencyInfoScreen
import com.example.parentcoachbot.feature_chat.presentation.profile_screen.AccountSetupScreen
import com.example.parentcoachbot.feature_chat.presentation.profile_screen.AddProfileScreen
import com.example.parentcoachbot.feature_chat.presentation.profile_screen.CreateProfileSplashScreen
import com.example.parentcoachbot.feature_chat.presentation.profile_screen.PinEntryScreen
import com.example.parentcoachbot.feature_chat.presentation.profile_screen.ProfileViewModel
import com.example.parentcoachbot.feature_chat.presentation.profile_screen.SelectProfileScreen
import com.example.parentcoachbot.feature_chat.presentation.profile_screen.UpdateProfileScreen
import com.example.parentcoachbot.feature_chat.presentation.resources_screens.ImageResourceScreen
import com.example.parentcoachbot.feature_chat.presentation.resources_screens.ResourcesHomeScreen
import com.example.parentcoachbot.feature_chat.presentation.saved_questions_screen.SavedQuestionsScreenEvent
import com.example.parentcoachbot.feature_chat.presentation.saved_questions_screen.SavedQuestionsViewModel
import com.example.parentcoachbot.feature_chat.presentation.settings_screen.ChangeCountryScreen
import com.example.parentcoachbot.feature_chat.presentation.settings_screen.DataPrivacyScreen
import com.example.parentcoachbot.feature_chat.presentation.settings_screen.SelectLanguageScreen
import com.example.parentcoachbot.feature_chat.presentation.settings_screen.SettingsHomeScreen
import com.example.parentcoachbot.feature_chat.presentation.settings_screen.TermsOfUseScreen
import com.example.parentcoachbot.feature_chat.presentation.splash_screens.FirstTimeSplashScreen
import com.example.parentcoachbot.feature_chat.presentation.splash_screens.OnboardingScreen
import com.example.parentcoachbot.feature_chat.presentation.splash_screens.SplashScreenViewModel
import com.example.parentcoachbot.ui.theme.OnboardingPageItem
import com.google.firebase.analytics.FirebaseAnalytics

@Composable
fun Navigation() {
    val navHostController: NavHostController = rememberNavController()
    val chatListViewModel = hiltViewModel<ChatListViewModel>()
    val chatViewModel: ChatViewModel = hiltViewModel()
    val contactScreenViewModel: ContactScreenViewModel = hiltViewModel()
    val savedQuestionsViewModel: SavedQuestionsViewModel = hiltViewModel()
    val splashScreenViewModel: SplashScreenViewModel = hiltViewModel()
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val firebaseAnalytics: FirebaseAnalytics = profileViewModel.firebaseAnalytics
    val currentParentUser by profileViewModel.profileViewModelState.value.parentUserState.collectAsStateWithLifecycle()
    val isTourCompleted by splashScreenViewModel.splashScreenViewModelState.value.isTourComplete.collectAsStateWithLifecycle()

    // firebaseAnalytics.setUserId(currentParentUser?.username ?: "Unknown")
    println("The username is ${firebaseAnalytics}")

    DisposableEffect(Unit) {
        var screenEntryTime = 0L


        val listener = NavController.OnDestinationChangedListener { controller, destination, _ ->
            val entryParams = Bundle()
            val destinationRoute = destination.route
            val previousScreenRoute = controller.previousBackStackEntry?.destination?.route
            val destinationChangeTime = System.currentTimeMillis()

            previousScreenRoute?.let {
                val exitParams = Bundle()
                val exitTime: Long = destinationChangeTime
                val parentUsername = currentParentUser?.username ?: "null"
                val authID = currentParentUser?.owner_id ?: "null"
                val timeSpentOnScreen =
                    if (screenEntryTime != 0L) exitTime - screenEntryTime else 0L
                exitParams.putString("SCREEN_NAME", previousScreenRoute)
                exitParams.putString("username", parentUsername)
                exitParams.putString("auth_id", authID)
                exitParams.putBoolean("Backgrounded", false)
                exitParams.putLong("TIME_ON_SCREEN", timeSpentOnScreen)

                firebaseAnalytics.logEvent("SCREEN_EXIT", exitParams)
            }


            destinationRoute?.let {
                screenEntryTime = System.currentTimeMillis()
                val parentUsername = currentParentUser?.username ?: "null"
                val authID = currentParentUser?.owner_id ?: "null"


                entryParams.putString(FirebaseAnalytics.Param.SCREEN_NAME, destinationRoute)
                entryParams.putString("PREVIOUS_SCREEN", previousScreenRoute ?: "null")
                entryParams.putString("username", parentUsername)
                entryParams.putString("authID", authID)

                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, entryParams)
            }

        }

        navHostController.addOnDestinationChangedListener(listener)

        // Dispose of the listener when the composable is no longer in the composition
        onDispose {

            val previousScreenRoute = navHostController.previousBackStackEntry?.destination?.route
            val backgroundedTime = System.currentTimeMillis()

            val exitParams = Bundle()
            val parentUsername = currentParentUser?.username ?: "null"
            val authID = currentParentUser?.owner_id ?: "null"

            val timeSpentOnScreen =
                if (screenEntryTime != 0L) backgroundedTime - screenEntryTime else 0L
            exitParams.putString("SCREEN_NAME", previousScreenRoute)
            exitParams.putString("username", parentUsername)
            exitParams.putString("auth_id", authID)
            exitParams.putBoolean("Backgrounded", true)
            exitParams.putLong("TIME_ON_SCREEN", timeSpentOnScreen)

            firebaseAnalytics.logEvent("SCREEN_EXIT", exitParams)

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
        startDestination = if (isTourCompleted) Screen.PinEntryScreen.route else Screen.FirstTimeSplashScreen.route
    ) {
        composable(route = Screen.FirstTimeSplashScreen.route) {
            FirstTimeSplashScreen(navController = navHostController,
                splashScreenViewModelState = splashScreenViewModel.splashScreenViewModelState,
                onEvent = { chatEvent -> chatViewModel.onEvent(chatEvent) })
        }

        composable(route = Screen.SelectProfileScreen.route) {
            SelectProfileScreen(navController = navHostController,
                profileState = profileViewModel.profileViewModelState,
                onEvent = { profileEvent ->
                    profileViewModel.onEvent(profileEvent)
                    // todo WHY is it really necessary to trigger this - can we observe and trigger directly
                    chatListViewModel.onEvent(chatListEvent = ChatListEvent.UpdateChildProfile)
                    savedQuestionsViewModel.onEvent(SavedQuestionsScreenEvent.UpdateChildProfile)
                    // todo trigger list update on saved screen - must do this for all things depending on global state

                })
        }

        composable(route = Screen.AccountSetupScreen.route) {
            AccountSetupScreen(
                navController = navHostController,
                onEvent = { profileEvent -> profileViewModel.onEvent(profileEvent) },
                profileState = profileViewModel.profileViewModelState
            )
        }

        composable(route = Screen.ExploreOnboardingScreen.route) {
            OnboardingScreen(
                onboardingPageItem = OnboardingPageItem.ExploreTopics,
                navController = navHostController,
                onEvent = { splashScreenEvent -> splashScreenViewModel.onEvent(splashScreenEvent) }
            )
        }

        composable(route = Screen.PinEntryScreen.route) {
            PinEntryScreen(
                navController = navHostController,
                profileState = profileViewModel.profileViewModelState
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
            CreateProfileSplashScreen(
                navController = navHostController,
                onEvent = { splashScreenEvent -> splashScreenViewModel.onEvent(splashScreenEvent) })
        }

        composable(route = Screen.UpdateProfileScreen.route) {
            UpdateProfileScreen(
                navController = navHostController,
                profileViewModel.profileViewModelState,
                onEvent = { profileEvent ->
                    profileViewModel.onEvent(profileEvent)
                }
            )

        }

        composable(route = Screen.AddProfileScreen.route) {
            AddProfileScreen(navController = navHostController,
                profileState = profileViewModel.profileViewModelState,
                onEvent = { profileEvent ->
                    profileViewModel.onEvent(profileEvent)
                    chatListViewModel.onEvent(chatListEvent = ChatListEvent.UpdateChildProfile)
                    savedQuestionsViewModel.onEvent(SavedQuestionsScreenEvent.UpdateChildProfile)
                    // todo trigger list update on saved screen - must do this for all things depending on global state
                })
        }

        composable(route = Screen.SettingsHomeScreen.route) {
            SettingsHomeScreen(
                navController = navHostController,
                profileViewModel.profileViewModelState
            )
        }

        composable(route = Screen.ChangeCountryScreen.route) {
            ChangeCountryScreen(
                navController = navHostController,
                onEvent = { profileEvent -> profileViewModel.onEvent(profileEvent) },
                profileState = profileViewModel.profileViewModelState
            )
        }

        composable(route = Screen.SelectLanguageScreen.route) {
            SelectLanguageScreen(navController = navHostController,
                chatViewModelState = chatViewModel.chatViewModelState,
                onEvent = { chatEvent -> chatViewModel.onEvent(chatEvent) })
        }

        composable(route = Screen.SavedQuestionsScreen.route) {
            SavedQuestionsScreen(
                navController = navHostController,
                savedQuestionsViewModelState = savedQuestionsViewModel.savedChatViewModelState,
                onEvent = { savedScreenEvent -> savedQuestionsViewModel.onEvent(savedScreenEvent) }
            )
        }

        composable(route = Screen.ResourcesHomeScreen.route) {
            ResourcesHomeScreen(navController = navHostController,
                chatListViewModelState = chatListViewModel.chatListViewModelState,
                onEvent = { chatListEvent ->
                    chatListViewModel.onEvent(chatListEvent)
                })
        }

        composable(route = Screen.ImageResourceScreen.route) {
            ImageResourceScreen(
                navController = navHostController,
                chatListViewModelState = chatListViewModel.chatListViewModelState)
        }

        composable(route = Screen.EmergencyInfoScreen.route) {
            EmergencyInfoScreen(
                navController = navHostController,
                profileState = profileViewModel.profileViewModelState,
                onEvent = { contactScreenEvent -> contactScreenViewModel.onEvent(contactScreenEvent) }
            )
        }

        composable(route = Screen.DataPrivacyScreen.route) {
            DataPrivacyScreen(
                navController = navHostController,
                profileState = profileViewModel.profileViewModelState
            )
        }

        composable(route = Screen.TermsOfUseScreen.route) {
            TermsOfUseScreen(
                navController = navHostController,
                profileState = profileViewModel.profileViewModelState
            )
        }

    }
}