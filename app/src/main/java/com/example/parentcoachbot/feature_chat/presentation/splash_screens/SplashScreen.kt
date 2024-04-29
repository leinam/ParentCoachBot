package com.example.parentcoachbot.feature_chat.presentation.splash_screens

import android.content.Intent
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.parentcoachbot.MainActivity
import com.example.parentcoachbot.R
import com.example.parentcoachbot.feature_chat.presentation.Screen
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.ChatEvent
import com.example.parentcoachbot.feature_chat.presentation.settings_screen.languageList
import com.example.parentcoachbot.ui.theme.BackgroundBeige
import com.example.parentcoachbot.ui.theme.BackgroundWhite
import com.example.parentcoachbot.ui.theme.Beige
import com.example.parentcoachbot.ui.theme.LightBeige
import com.example.parentcoachbot.ui.theme.LightGreen
import com.example.parentcoachbot.ui.theme.PlexSans
import com.example.parentcoachbot.ui.theme.PrimaryGreen
import com.example.parentcoachbot.ui.theme.TextGrey

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun FirstTimeSplashScreen(
    splashScreenViewModelState: State<SplashScreenStateWrapper> = mutableStateOf(
        SplashScreenStateWrapper()
    ),
    navController: NavController = rememberNavController(),
    onEvent: (chatEvent: ChatEvent) -> Unit = {}
) {

    val splashScreenStateWrapper = splashScreenViewModelState.value
    var isExpanded by remember {
        mutableStateOf(false)
    }

    val application by splashScreenStateWrapper.application.collectAsStateWithLifecycle()
    val context = application?.applicationContext
    val currentLanguageCode =
        splashScreenStateWrapper.currentLanguageCode.collectAsStateWithLifecycle()



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = PrimaryGreen)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.End
            )
            {
                ExposedDropdownMenuBox(
                    modifier = Modifier
                        .width(190.dp),
                    expanded = isExpanded,
                    onExpandedChange = {
                        isExpanded = !isExpanded
                    })
                {


                    TextField(
                        readOnly = true,
                        value = stringResource(
                            id = languageList.filter { language -> language.isoCode == currentLanguageCode.value }[0].name
                        ),
                        onValueChange = { },
                        label = {
                            Text(
                                text = stringResource(id = R.string.language_label),
                                fontSize = 10.sp
                            )
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = isExpanded
                            )
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_language_24),
                                contentDescription = null
                            )
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(
                            unfocusedContainerColor = LightGreen,
                            focusedContainerColor = LightGreen,
                            unfocusedTextColor = BackgroundBeige,
                            focusedTextColor = BackgroundBeige,
                            focusedLeadingIconColor = BackgroundWhite,
                            unfocusedLeadingIconColor = BackgroundWhite,
                            focusedLabelColor = TextGrey
                        ),
                        modifier = Modifier.menuAnchor()
                    )

                    ExposedDropdownMenu(expanded = isExpanded,
                        onDismissRequest = { isExpanded = false }) {

                        languageList.forEach { language ->
                            DropdownMenuItem(text = { Text(text = stringResource(language.name)) },
                                onClick = {
                                    onEvent(ChatEvent.ChangeLanguage(language.isoCode)).also {
                                        val intent = Intent(context, MainActivity::class.java)
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        context?.startActivity(intent)
                                    }
                                })
                        }
                    }

                }
            }

            Spacer(modifier = Modifier.size(42.dp))

            Box(modifier = Modifier) {
                Icon(
                    tint = Beige, painter =
                    painterResource(id = R.drawable.pclogo),
                    contentDescription = "Aurora Logo",
                    modifier = Modifier
                        .size(130.dp)
                        .align(Alignment.Center)
                )

            }

            Spacer(modifier = Modifier.size(30.dp))

            Text(
                text = stringResource(id = R.string.welcome),
                textAlign = TextAlign.Center,
                fontFamily = PlexSans,
                fontWeight = FontWeight.Medium,
                fontSize = 36.sp,
                lineHeight = 45.sp,
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
                    .padding(10.dp)

            ) {
                Text(
                    text = stringResource(id = R.string.app_intro_message),
                    textAlign = TextAlign.Start,
                    fontFamily = PlexSans,
                    fontWeight = FontWeight.Normal,
                    fontSize = 19.sp, modifier = Modifier.padding(5.dp)
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
                        navController.navigate(Screen.PinEntryScreen.route) {
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