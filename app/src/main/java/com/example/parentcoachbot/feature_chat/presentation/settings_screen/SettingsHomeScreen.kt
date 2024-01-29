package com.example.parentcoachbot.feature_chat.presentation.settings_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.parentcoachbot.R
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.CustomNavigationDrawer
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.TopNavBar
import com.example.parentcoachbot.feature_chat.presentation.profile_screen.ProfileStateWrapper
import com.example.parentcoachbot.ui.theme.Beige
import com.example.parentcoachbot.ui.theme.PlexSans
import com.example.parentcoachbot.ui.theme.PrimaryGreen
import com.example.parentcoachbot.ui.theme.TextGrey
import com.example.parentcoachbot.ui.theme.ThinGreen
import com.example.parentcoachbot.ui.theme.settingsItemList

@Preview
@Composable
fun SettingsHomeScreen(
    navController: NavController = rememberNavController(),
    profileState: State<ProfileStateWrapper> = mutableStateOf(
        ProfileStateWrapper()
    )
) {

    val profileStateWrapper:ProfileStateWrapper = profileState.value
    val parentUser by profileStateWrapper.parentUserState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    var drawerSelectedItemIndex = rememberSaveable { mutableIntStateOf(5) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)



    Scaffold(
        topBar = {
            TopNavBar(
                navController = navController,
                screenIndex = 6,
                drawerState = drawerState,
                scope = scope
            )
        },

        )
    { contentPadding ->
        CustomNavigationDrawer(
            drawerState = drawerState,
            drawerSelectedItemIndex = drawerSelectedItemIndex,
            navController = navController,
            contentPadding = contentPadding,
            content = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Beige)
                        .padding(contentPadding)

                ) {

                    Column {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(PrimaryGreen.copy(alpha = 0.3f))
                                .padding(17.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = stringResource(id = R.string.settings_label),
                                color = PrimaryGreen,
                                fontFamily = PlexSans,
                                fontWeight = FontWeight.Normal,
                                fontSize = 19.sp
                            )
                        }

                        Text(
                            text = stringResource(id = R.string.participant_username_label) + ": ${parentUser?.username}",
                            color = PrimaryGreen,
                            fontFamily = PlexSans,
                            fontWeight = FontWeight.Normal,
                            fontSize = 19.sp,
                            modifier = Modifier.padding(10.dp)
                        )

                        LazyColumn {
                            items(settingsItemList) { item ->
                                Box(modifier = Modifier
                                    .padding(8.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(color = ThinGreen)
                                    .fillMaxWidth()
                                    .height(70.dp)
                                    .padding(10.dp)
                                    .clickable {
                                        item.route?.let {
                                            navController.navigate(it)
                                        }
                                    }
                                )
                                {

                                    Row(
                                        modifier = Modifier
                                            .align(Alignment.CenterStart)
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {


                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                painter = painterResource(id = item.icon),
                                                contentDescription = null,
                                                tint = TextGrey,
                                                modifier = Modifier.padding(end = 12.dp)
                                            )

                                            item.title?.let {
                                                Text(
                                                    text = stringResource(id = it),
                                                    fontSize = 18.sp,
                                                    fontFamily = PlexSans,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = TextGrey,

                                                    )
                                            }
                                        }

                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowRight,
                                            contentDescription = null,
                                            tint = TextGrey,

                                            )


                                    }


                                }
                            }
                        }


                    }

                }
            })

    }
}
