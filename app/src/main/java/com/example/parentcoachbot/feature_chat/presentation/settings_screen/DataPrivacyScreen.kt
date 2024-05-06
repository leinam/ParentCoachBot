package com.example.parentcoachbot.feature_chat.presentation.settings_screen

import androidx.compose.foundation.background
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.parentcoachbot.R
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.CustomNavigationDrawer
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.TopNavBar
import com.example.parentcoachbot.feature_chat.presentation.contact_screen.ExpandableCard
import com.example.parentcoachbot.feature_chat.presentation.profile_screen.ProfileStateWrapper
import com.example.parentcoachbot.ui.theme.Beige
import com.example.parentcoachbot.ui.theme.PlexSans
import com.example.parentcoachbot.ui.theme.PrimaryGreen
import com.example.parentcoachbot.ui.theme.TextGrey

@Preview
@Composable
fun DataPrivacyScreen(
    navController: NavController = rememberNavController(),
    profileState: State<ProfileStateWrapper> = mutableStateOf(ProfileStateWrapper())
) {

    val profileStateWrapper = profileState.value

    val parentUser by profileStateWrapper.parentUserState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val drawerSelectedItemIndex = rememberSaveable() {
        mutableIntStateOf(5)
    }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val context = LocalContext.current



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
                                text = stringResource(R.string.data_privacy_label),
                                color = PrimaryGreen,
                                fontFamily = PlexSans,
                                fontWeight = FontWeight.Normal,
                                fontSize = 19.sp
                            )


                        }

                        LazyColumn() {

                            item {
                                ExpandableCard(
                                    cardHeaderStringId = R.string.data_privacy_label,
                                    content = {
                                        if (parentUser?.country == "South Africa"){
                                            Text(
                                                text = stringResource(id = R.string.data_privacy_introduction_header),
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextGrey,
                                                modifier = Modifier
                                                    .padding(10.dp)

                                            )

                                            Text(
                                                text = stringResource(id = R.string.data_privacy_introduction_sa),
                                                fontSize = 15.sp,
                                                color = TextGrey,
                                                modifier = Modifier.padding(10.dp)

                                            )

                                            Text(
                                                text = stringResource(id = R.string.data_privacy_why_header),
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextGrey,
                                                modifier = Modifier
                                                    .padding(10.dp)

                                            )

                                            Text(
                                                text = stringResource(id = R.string.data_privacy_why_description),
                                                fontSize = 15.sp,
                                                color = TextGrey,
                                                modifier = Modifier.padding(10.dp)

                                            )

                                            Text(
                                                text = stringResource(id = R.string.data_protection_law_header),
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextGrey,
                                                modifier = Modifier
                                                    .padding(10.dp)

                                            )

                                            Text(
                                                text = stringResource(id = R.string.data_protection_law_text),
                                                fontSize = 15.sp,
                                                color = TextGrey,
                                                modifier = Modifier.padding(10.dp)

                                            )
                                        } else {

                                            Text(
                                                text = stringResource(id = R.string.data_privacy_introduction_header),
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextGrey,
                                                modifier = Modifier
                                                    .padding(10.dp)

                                            )

                                            Text(
                                                text = stringResource(id = R.string.data_privacy_introduction_pt),
                                                fontSize = 15.sp,
                                                color = TextGrey,
                                                modifier = Modifier.padding(10.dp)

                                            )

                                            Text(
                                                text = stringResource(id = R.string.data_privacy_why_header),
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextGrey,
                                                modifier = Modifier
                                                    .padding(10.dp)

                                            )

                                            Text(
                                                text = stringResource(id = R.string.data_privacy_why_pt),
                                                fontSize = 15.sp,
                                                color = TextGrey,
                                                modifier = Modifier.padding(10.dp)

                                            )

                                            Text(
                                                text = stringResource(id = R.string.data_protection_law_header),
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextGrey,
                                                modifier = Modifier
                                                    .padding(10.dp)

                                            )

                                            Text(
                                                text = stringResource(id = R.string.data_protection_law_pt),
                                                fontSize = 15.sp,
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