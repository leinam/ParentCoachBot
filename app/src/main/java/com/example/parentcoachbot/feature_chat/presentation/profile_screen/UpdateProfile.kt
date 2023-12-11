package com.example.parentcoachbot.feature_chat.presentation.profile_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.parentcoachbot.R
import com.example.parentcoachbot.feature_chat.presentation.Screen
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.TopNavBar
import com.example.parentcoachbot.ui.theme.BackgroundWhite
import com.example.parentcoachbot.ui.theme.Beige
import com.example.parentcoachbot.ui.theme.ChatListGreen
import com.example.parentcoachbot.ui.theme.LightBeige
import com.example.parentcoachbot.ui.theme.PlexSans
import com.example.parentcoachbot.ui.theme.PrimaryGreen

@Composable
@Preview
fun CreateProfileSplashScreen(navController: NavController = rememberNavController()) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = PrimaryGreen)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Box(modifier = Modifier) {
                Icon(
                    tint = Beige, painter =
                    painterResource(id = R.drawable.pclogo),
                    contentDescription = "ParentCoach Logo",
                    modifier = Modifier.size(130.dp)
                )

            }

            Spacer(modifier = Modifier.size(30.dp))

            Text(
                text = stringResource(id = R.string.create_profile_onboarding_header),
                textAlign = TextAlign.Center,
                fontFamily = PlexSans,
                lineHeight = 45.sp,
                fontWeight = FontWeight.Medium,
                fontSize = 33.sp,
                color = Beige,
                modifier = Modifier.padding(10.dp)
            )

            Spacer(modifier = Modifier.size(30.dp))

            Box(
                modifier = Modifier
                    .width(300.dp)
                    .padding(20.dp)
                    .clip(
                        RoundedCornerShape(
                            topEnd = 20.dp,
                            bottomStart = 20.dp,
                            bottomEnd = 20.dp
                        )
                    )
                    .background(color = LightBeige)
                    .padding(20.dp)
                    .clickable {

                    }
            ) {
                Text(
                    text = stringResource(R.string.create_profile_onboarding_description),
                    textAlign = TextAlign.Start,
                    fontFamily = PlexSans,
                    fontWeight = FontWeight.Normal,
                    fontSize = 19.sp,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp)
                )


            }

            Spacer(modifier = Modifier.size(30.dp))

            Box(
                modifier = Modifier
                    .width(180.dp)
                    .padding(10.dp)
                    .clip(
                        RoundedCornerShape(30.dp)
                    )
                    .clickable {
                        navController.navigate(Screen.SelectProfileScreen.route) {
                            popUpTo(Screen.FirstTimeSplashScreen.route) {
                                inclusive = true
                            }
                        }
                    }
                    .background(color = Beige)
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.create_profile_button_label).uppercase(),
                    textAlign = TextAlign.Center,
                    fontFamily = PlexSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 19.sp,
                    color = PrimaryGreen
                )
            }
        }

    }

}

@Preview
@Composable
fun UpdateProfileScreen(
    navController: NavController = rememberNavController(),
    profileState: State<ProfileStateWrapper> = mutableStateOf(ProfileStateWrapper())
) {
    var name: String? by rememberSaveable {
        mutableStateOf(null)
    }

    val profileStateWrapper = profileState.value
    val currentChildProfile by profileStateWrapper.currentChildProfileState.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    Scaffold(topBar = {
        TopNavBar(
            screenIndex = 3,
            scope = scope,
            navController = navController
        )
    })
    { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Beige)
                .padding(contentPadding)
        ) {

            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ChatListGreen)
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = stringResource(id = R.string.update_profile_label),
                        color = PrimaryGreen,
                        fontFamily = PlexSans,
                        fontWeight = FontWeight.Normal,
                        fontSize = 19.sp
                    )


                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    currentChildProfile?.let {

                        Icon(
                            painter = painterResource(id = R.drawable.breastfeeding_icon),
                            contentDescription = null, tint = PrimaryGreen,
                            modifier = Modifier.size(100.dp)
                        )

                        Text(
                            text = it.name ?: "",
                            color = PrimaryGreen, fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold
                        )

                    }

Spacer(modifier = Modifier.size(20.dp))

                    TextField(
                        value = name ?: "",
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = BackgroundWhite,
                            focusedContainerColor = BackgroundWhite,
                        ),
                        label = { Text(text = stringResource(id = R.string.child_name_label)) },
                        placeholder = { Text(text = stringResource(id = R.string.name_placeholder)) },
                        onValueChange = { name = it },
                        textStyle = LocalTextStyle.current.copy()
                    )

                    Spacer(modifier = Modifier.size(60.dp))
                    // TODO add date picker

                    Box(
                        modifier = Modifier
                            .width(180.dp)
                            .padding(10.dp)
                            .clip(
                                RoundedCornerShape(30.dp)
                            )
                            .background(color = PrimaryGreen)
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.update_profile_button_label).uppercase(),
                            textAlign = TextAlign.Center,
                            fontFamily = PlexSans,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            color = Beige
                        )
                    }


                }


            }
        }


    }

}