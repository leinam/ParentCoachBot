package com.example.parentcoachbot.feature_chat.presentation

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.example.parentcoachbot.MainActivity
import com.example.parentcoachbot.R
import com.example.parentcoachbot.feature_chat.domain.util.Language
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.ChatEvent
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.ChatStateWrapper
import com.example.parentcoachbot.ui.theme.Beige
import com.example.parentcoachbot.ui.theme.LightBeige
import com.example.parentcoachbot.ui.theme.PlexSans
import com.example.parentcoachbot.ui.theme.PrimaryGreen

@Preview
@Composable
fun FirstTimeSplashScreen(
    splashScreenViewModelState: State<SplashScreenStateWrapper> = mutableStateOf(SplashScreenStateWrapper()),
    navController: NavController = rememberNavController(),
    onEvent: (chatEvent: ChatEvent) -> Unit = {}
) {

    val splashScreenStateWrapper = splashScreenViewModelState.value

    val application by splashScreenStateWrapper.application.collectAsStateWithLifecycle()
    val context = application?.applicationContext

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
                    contentDescription = "Aurora Logo",
                    modifier = Modifier
                        .size(175.dp)
                        .align(Alignment.Center)
                )

            }

            Spacer(modifier = Modifier.size(30.dp))

            Text(
                text = "ParentCoach",
                textAlign = TextAlign.Center,
                fontFamily = PlexSans,
                fontWeight = FontWeight.Medium,
                fontSize = 45.sp,
                color = Beige
            )

            Spacer(modifier = Modifier.size(25.dp))

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
                    .padding(15.dp)

            ) {
                Text(
                    text = stringResource(id = R.string.app_intro_message),
                    textAlign = TextAlign.Start,
                    fontFamily = PlexSans,
                    fontWeight = FontWeight.Normal,
                    fontSize = 19.sp
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(10.dp)
            ) {
                Text(
                    text = "PT",
                    color = Beige,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(15.dp)
                        .clickable {
                            onEvent(ChatEvent.ChangeLanguage(Language.Portuguese.isoCode)).also {
                                val intent = Intent(context, MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                context?.startActivity(intent)
                            }
                        }
                )
                Text(
                    text = "EN",
                    color = Beige,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(15.dp).clickable {
                        onEvent(ChatEvent.ChangeLanguage(Language.English.isoCode)).also {
                            val intent = Intent(context, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            context?.startActivity(intent)
                        }
                    }
                )
                Text(
                    text = "ZU",
                    color = Beige,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(15.dp).clickable {
                        onEvent(ChatEvent.ChangeLanguage(Language.Zulu.isoCode)).also {
                            val intent = Intent(context, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            context?.startActivity(intent)
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.size(10.dp))

            Box(
                modifier = Modifier
                    .width(180.dp)
                    .padding(10.dp)
                    .clip(
                        RoundedCornerShape(30.dp)
                    )
                    .background(color = Beige)
                    .padding(10.dp)
                    .clickable {
                        navController.navigate(Screen.SelectProfileScreen.route) {
                            popUpTo(Screen.FirstTimeSplashScreen.route) {
                                inclusive = true
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.get_started_button_text).uppercase(),
                    textAlign = TextAlign.Center,
                    fontFamily = PlexSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 19.sp,
                    color = PrimaryGreen
                )
            }


            Spacer(modifier = Modifier.size(15.dp))

            Text(text = stringResource(id = R.string.tour_button_text).uppercase(),
                textAlign = TextAlign.Center,
                fontFamily = PlexSans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = Beige,
                modifier = Modifier.clickable {
                    navController.navigate(route = Screen.ExploreOnboardingScreen.route) {
                        popUpTo(Screen.FirstTimeSplashScreen.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }


    }

}


@Preview
@Composable
fun SplashScreen(navController: NavController = rememberNavController()) {
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
                    painterResource(id = R.drawable.pclogo2),
                    contentDescription = "Aurora Logo", modifier = Modifier.size(225.dp)
                )

            }

        }


    }

}