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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.parentcoachbot.R
import com.example.parentcoachbot.ui.theme.Beige
import com.example.parentcoachbot.ui.theme.LightBeige
import com.example.parentcoachbot.ui.theme.LightGreen
import com.example.parentcoachbot.ui.theme.OnboardingPageItem
import com.example.parentcoachbot.ui.theme.PlexSans
import com.example.parentcoachbot.ui.theme.PrimaryGreen

@Preview
@Composable
fun OnboardingScreen(onboardingPageItem: OnboardingPageItem = OnboardingPageItem.ExploreTopics,
                     navController: NavController = rememberNavController()) {

    val headerText: String = stringResource(onboardingPageItem.headerText)
    val descriptionText: String = stringResource(onboardingPageItem.descriptionText)
    val pageIndex: Int = onboardingPageItem.pageIndex

    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Beige))
    {
        Column(modifier = Modifier
            .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {

            Box(modifier = Modifier){
                Icon(tint = PrimaryGreen, painter =
                painterResource(id = R.drawable.logo_sleepy),
                    contentDescription ="Aurora Logo")
            }

            Spacer(modifier = Modifier.size(30.dp))

            Text(text = headerText,
                textAlign = TextAlign.Center,
                fontFamily = PlexSans,
                fontWeight = FontWeight.Medium,
                fontSize = 36.sp,
                color = PrimaryGreen
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
                Text(text = descriptionText,
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
                    .background(color = PrimaryGreen)
                    .padding(10.dp)
                    .clickable {
                        when (onboardingPageItem) {
                            OnboardingPageItem.ExploreTopics -> navController.navigate(route = Screen.SearchOnboardingScreen.route)
                            OnboardingPageItem.SaveFavourites -> navController.navigate(route = Screen.ChatListScreen.route)
                            OnboardingPageItem.SearchQuestions -> navController.navigate(route = Screen.FavouriteOnboardingScreen.route)
                        }
                    },
                contentAlignment = Alignment.Center
            ){
                Text(text = stringResource(id = R.string.continue_label).uppercase(),
                    textAlign = TextAlign.Center,
                    fontFamily = PlexSans,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 19.sp,
                    color = Beige
                )
            }

            Spacer(modifier = Modifier.size(15.dp))

            Text(text = stringResource(id = R.string.skip_label).uppercase(),
                textAlign = TextAlign.Center,
                fontFamily = PlexSans,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = PrimaryGreen,
                modifier = Modifier.clickable {
                    navController.navigate(route = Screen.ChatListScreen.route)
                }
            )

            Spacer(modifier = Modifier.size(60.dp))

            LazyRow(){
                items(3){
                    selectedIndex ->
                        if (pageIndex == selectedIndex) {
                            Box(modifier = Modifier
                                .padding(10.dp)
                                .size(15.dp)
                                .clip(CircleShape)
                                .background(PrimaryGreen)
                                .padding(10.dp))
                        } else {
                            Box(modifier = Modifier
                                .padding(10.dp)
                                .size(15.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(LightGreen))
                        }
                }
            }
        }
    }
}