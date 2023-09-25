package com.example.parentcoachbot.feature_chat.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.parentcoachbot.R
import com.example.parentcoachbot.ui.theme.Beige
import com.example.parentcoachbot.ui.theme.LightBeige
import com.example.parentcoachbot.ui.theme.PlexSans
import com.example.parentcoachbot.ui.theme.PrimaryGreen

@Preview
@Composable
fun FirstTimeSplashScreen(navController: NavController = rememberNavController()) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = PrimaryGreen)){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {

            Box(modifier = Modifier){
                Icon(tint = Beige, painter =
                painterResource(id = R.drawable.logo_sleepy),
                    contentDescription ="Aurora Logo")

            }

            Spacer(modifier = Modifier.size(30.dp))

            Text(text = "ParentCoach",
                textAlign = TextAlign.Center,
                fontFamily = PlexSans,
                fontWeight = FontWeight.Medium,
                fontSize = 45.sp,
                color = Beige
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
                    .padding(15.dp)

            ) {
                Text(text = "A chatbot that will help you learn and clarify doubts about childcare health.",
                    textAlign = TextAlign.Start,
                    fontFamily = PlexSans,
                    fontWeight = FontWeight.Normal,
                    fontSize = 19.sp
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
                    .background(color = Beige)
                    .padding(10.dp)
                    .clickable {
                        navController.navigate(Screen.SelectProfileScreen.route){
                            popUpTo(Screen.FirstTimeSplashScreen.route) {
                                inclusive = true
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ){
                Text(text = "GET STARTED",
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
fun SplashScreen(navController: NavController = rememberNavController()) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = PrimaryGreen)){
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {

            Box(modifier = Modifier){
                Icon(tint = Beige, painter =
                painterResource(id = R.drawable.parentcoach_vetorial_logo),
                    contentDescription ="Aurora Logo")

            }

        }



    }

}