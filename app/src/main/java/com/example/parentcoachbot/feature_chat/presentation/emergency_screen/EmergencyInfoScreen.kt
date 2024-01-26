package com.example.parentcoachbot.feature_chat.presentation.emergency_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DrawerValue
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.parentcoachbot.R
import com.example.parentcoachbot.feature_chat.domain.util.PhoneDialerManager
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.CustomNavigationDrawer
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.TopNavBar
import com.example.parentcoachbot.feature_chat.presentation.profile_screen.ProfileStateWrapper
import com.example.parentcoachbot.ui.theme.Beige
import com.example.parentcoachbot.ui.theme.PlexSans
import com.example.parentcoachbot.ui.theme.PrimaryGreen
import com.example.parentcoachbot.ui.theme.TextGrey

@Preview
@Composable
fun EmergencyInfoScreen(
    navController: NavController = rememberNavController(),
    profileState: State<ProfileStateWrapper> = mutableStateOf(ProfileStateWrapper())
) {

    val profileStateWrapper = profileState.value

    val parentUser by profileStateWrapper.parentUserState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val drawerSelectedItemIndex = rememberSaveable() {
        mutableIntStateOf(2)
    }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val context = LocalContext.current



    Scaffold(
        topBar = {
            TopNavBar(
                navController = navController,
                screenIndex = 1,
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
            content =
            {
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
                                text = stringResource(R.string.emergency_info_label),
                                color = PrimaryGreen,
                                fontFamily = PlexSans,
                                fontWeight = FontWeight.Normal,
                                fontSize = 19.sp
                            )


                        }

                        LazyColumn() {

                            item {
                                ExpandableCard(
                                    cardHeaderStringId = R.string.medical_emergency_label,
                                    content = {
                                        if (parentUser?.country == "South Africa") {
                                            Text(
                                                text = "South Africa",
                                                fontSize = 20.sp,
                                                color = PrimaryGreen,
                                                modifier = Modifier.padding(10.dp)
                                            )
                                            Text(

                                                text = stringResource(id = R.string.sa_emergency_contact),
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                textDecoration = TextDecoration.Underline,
                                                color = TextGrey,
                                                modifier = Modifier
                                                    .padding(10.dp)
                                                    .clickable {
                                                        PhoneDialerManager.openDialPad(
                                                            context = context,
                                                            phoneNum = "10177"
                                                        )
                                                    }
                                            )
                                            Text(
                                                text = stringResource(id = R.string.sa_emergency_contact_description),
                                                fontSize = 15.sp,
                                                color = TextGrey,
                                                modifier = Modifier.padding(10.dp)
                                            )
                                        } else {

                                            Text(
                                                text = "Portugal",
                                                fontSize = 20.sp,
                                                color = PrimaryGreen,
                                                modifier = Modifier.padding(10.dp)
                                            )

                                            Text(
                                                text = stringResource(id = R.string.pt_medical_emergency),
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextGrey,
                                                textDecoration = TextDecoration.Underline,
                                                modifier = Modifier
                                                    .padding(10.dp)
                                                    .clickable {
                                                        PhoneDialerManager.openDialPad(
                                                            context = context,
                                                            phoneNum = "112"
                                                        )
                                                    }
                                            )
                                            Text(
                                                text = stringResource(id = R.string.pt_medical_emergency_description),
                                                fontSize = 15.sp,
                                                color = TextGrey,
                                                modifier = Modifier.padding(10.dp)

                                            )
                                            Text(
                                                text = stringResource(id = R.string.pt_medical_emergency_2),
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextGrey,
                                                textDecoration = TextDecoration.Underline,
                                                modifier = Modifier
                                                    .padding(10.dp)
                                                    .clickable {
                                                        PhoneDialerManager.openDialPad(
                                                            context = context,
                                                            phoneNum = "808242424"
                                                        )
                                                    }
                                            )
                                            Text(
                                                text = stringResource(id = R.string.pt_medical_emergency_2_description),
                                                fontSize = 15.sp,
                                                color = TextGrey,
                                                modifier = Modifier.padding(10.dp)
                                            )
                                        }
                                    })
                            }

                            item {
                                ExpandableCard(
                                    cardHeaderStringId = R.string.mental_health_header,
                                    content = {
                                        if (parentUser?.country == "South Africa") {
                                            Text(
                                                text = "South Africa",
                                                fontSize = 20.sp,
                                                color = PrimaryGreen,
                                                modifier = Modifier.padding(10.dp)
                                            )
                                            Text(
                                                text = stringResource(id = R.string.sadag_contact),
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextGrey,
                                                textDecoration = TextDecoration.Underline,
                                                modifier = Modifier
                                                    .padding(10.dp)
                                                    .clickable {
                                                        PhoneDialerManager.openDialPad(
                                                            context = context,
                                                            phoneNum = "0800567567"
                                                        )
                                                    }
                                            )
                                            Text(
                                                text = stringResource(id = R.string.sadag_info),
                                                fontSize = 15.sp,
                                                color = TextGrey,
                                                modifier = Modifier.padding(10.dp)
                                            )
                                        } else {
                                            Text(
                                                text = "Portugal",
                                                fontSize = 20.sp,
                                                color = PrimaryGreen,
                                                modifier = Modifier.padding(10.dp)
                                            )

                                            Text(
                                                text = stringResource(id = R.string.pt_mental_health_contact),
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextGrey,
                                                modifier = Modifier.padding(10.dp)
                                            )

                                            Text(
                                                text = "808 24 24 24",
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextGrey,
                                                textDecoration = TextDecoration.Underline,
                                                modifier = Modifier
                                                    .padding(10.dp)
                                                    .clickable {
                                                        PhoneDialerManager.openDialPad(
                                                            context = context,
                                                            phoneNum = "808242424"
                                                        )
                                                    }
                                            )


                                            Text(
                                                text = stringResource(id = R.string.pt_mental_health_contact_description),
                                                fontSize = 15.sp,
                                                color = TextGrey,
                                                modifier = Modifier.padding(10.dp)
                                            )
                                            Text(
                                                text = stringResource(id = R.string.pt_mental_health_contact_2),
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextGrey,
                                                modifier = Modifier.padding(10.dp)
                                            )
                                            Text(
                                                text = "213 544 545",
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextGrey,
                                                textDecoration = TextDecoration.Underline,
                                                modifier = Modifier
                                                    .padding(10.dp)
                                                    .clickable {
                                                        PhoneDialerManager.openDialPad(
                                                            context = context,
                                                            phoneNum = "213544545"
                                                        )
                                                    }
                                            )

                                            Text(
                                                text = "912 802 669",
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextGrey,
                                                textDecoration = TextDecoration.Underline,
                                                modifier = Modifier
                                                    .padding(10.dp)
                                                    .clickable {
                                                        PhoneDialerManager.openDialPad(
                                                            context = context,
                                                            phoneNum = "912 802 669"
                                                        )
                                                    }
                                            )

                                            Text(
                                                text = "963 524 660",
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextGrey,
                                                textDecoration = TextDecoration.Underline,
                                                modifier = Modifier
                                                    .padding(10.dp)
                                                    .clickable {
                                                        PhoneDialerManager.openDialPad(
                                                            context = context,
                                                            phoneNum = "963 524 660"
                                                        )
                                                    }
                                            )

                                            Text(
                                                text = stringResource(id = R.string.pt_mental_health_contact_2_description),
                                                fontSize = 15.sp,
                                                color = TextGrey,
                                                modifier = Modifier.padding(10.dp)
                                            )
                                        }
                                    })
                            }

                            item {
                                ExpandableCard(
                                    cardHeaderStringId = R.string.team_contact_label,
                                    content = {
                                        if (parentUser?.country == "South Africa") {
                                            Text(
                                                text = "South Africa",
                                                fontSize = 20.sp,
                                                color = PrimaryGreen,
                                                modifier = Modifier.padding(10.dp)
                                            )

                                            Text(
                                                text = stringResource(id = R.string.research_contact_description),
                                                fontSize = 15.sp,
                                                color = TextGrey,
                                                modifier = Modifier.padding(10.dp)
                                            )

                                            Text(
                                                text = stringResource(id = R.string.sa_team_contact),
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextGrey,
                                                modifier = Modifier.padding(10.dp)
                                            )

                                        } else {
                                            Text(
                                                text = "Portugal",
                                                fontSize = 20.sp,
                                                color = PrimaryGreen,
                                                modifier = Modifier.padding(10.dp)
                                            )

                                            Text(
                                                text = stringResource(id = R.string.research_contact_description),
                                                fontSize = 15.sp,
                                                color = TextGrey,
                                                modifier = Modifier.padding(10.dp)
                                            )

                                            Text(
                                                text = stringResource(id = R.string.pt_team_contact),
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextGrey,
                                                modifier = Modifier.padding(10.dp)

                                            )
                                        }
                                    })
                            }


                        }
                    }


                }
            })
    }

}