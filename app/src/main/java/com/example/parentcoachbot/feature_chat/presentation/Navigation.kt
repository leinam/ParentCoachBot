package com.example.parentcoachbot.feature_chat.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.parentcoachbot.feature_chat.presentation.chat_list.ChatListScreen
import com.example.parentcoachbot.feature_chat.presentation.chat_list.ChatListViewModel
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.ChatScreen
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.ChatViewModel
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.CreateProfileSplashScreen
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.UpdateProfileScreen
import com.example.parentcoachbot.feature_chat.presentation.profile_screen.ProfileViewModel
import com.example.parentcoachbot.feature_chat.presentation.profile_screen.SelectProfileScreen
import com.example.parentcoachbot.ui.theme.OnboardingPageItem

@Composable
fun Navigation() {
    val navHostController: NavHostController = rememberNavController()

    NavHost(navController = navHostController,
        startDestination = Screen.SplashScreen.route ){
        composable(route=Screen.SplashScreen.route){
            SplashScreen(navController = navHostController)
        }

        composable(route=Screen.SelectProfileScreen.route){
            val profileViewModel: ProfileViewModel = hiltViewModel()

            SelectProfileScreen(navController = navHostController,
                profileStateWrapper = profileViewModel.profileStateWrapper,
                onEvent = {profileEvent -> profileViewModel.onEvent(profileEvent)})
        }

        composable(route = Screen.ExploreOnboardingScreen.route){
            OnboardingScreen(onboardingPageItem = OnboardingPageItem.ExploreTopics,
                navController = navHostController)
        }

        composable(route = Screen.SearchOnboardingScreen.route){
            OnboardingScreen(onboardingPageItem = OnboardingPageItem.SearchQuestions,
                navController = navHostController)
        }

        composable(route = Screen.FavouriteOnboardingScreen.route){
            OnboardingScreen(onboardingPageItem = OnboardingPageItem.SaveFavourites,
                navController = navHostController)
        }

        composable(route = Screen.ChatListScreen.route){
            val chatListViewModel = hiltViewModel<ChatListViewModel>()
            val chatViewModel = hiltViewModel<ChatViewModel>()


            ChatListScreen(navController = navHostController,
                chatListStateWrapper = chatListViewModel.chatListStateWrapper,
                onChatListEvent = {chatListEvent ->  chatListViewModel.onEvent(chatListEvent)},
                onChatEvent = {chatEvent -> chatViewModel.onEvent(chatEvent)} )
        }

        composable(route = Screen.ChatScreen.route){
            val chatViewModel = hiltViewModel<ChatViewModel>()

            ChatScreen(chatViewModelState = chatViewModel.chatStateWrapper,
                navController = navHostController)
        }

        composable(route = Screen.CreateProfileSplashScreen.route){
            CreateProfileSplashScreen(navController = navHostController)
        }

        composable(route = Screen.AddProfileScreen.route){
            UpdateProfileScreen(navController = navHostController)
        }
    }
}