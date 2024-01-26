package com.example.parentcoachbot.feature_chat.presentation.settings_screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
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
import com.example.parentcoachbot.feature_chat.domain.model.ParentUser
import com.example.parentcoachbot.feature_chat.presentation.Screen
import com.example.parentcoachbot.feature_chat.presentation.profile_screen.ProfileEvent
import com.example.parentcoachbot.feature_chat.presentation.profile_screen.ProfileStateWrapper
import com.example.parentcoachbot.feature_chat.presentation.profile_screen.countryList
import com.example.parentcoachbot.ui.theme.BackgroundBeige
import com.example.parentcoachbot.ui.theme.BackgroundWhite
import com.example.parentcoachbot.ui.theme.Beige
import com.example.parentcoachbot.ui.theme.LightGreen
import com.example.parentcoachbot.ui.theme.PlexSans
import com.example.parentcoachbot.ui.theme.PrimaryGreen
import com.example.parentcoachbot.ui.theme.TextGrey

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ChangeCountryScreen(
    navController: NavController = rememberNavController(),
    onEvent: (profileEvent: ProfileEvent) -> Unit = {},
    profileState: State<ProfileStateWrapper> = mutableStateOf(ProfileStateWrapper())
) {

    val profileStateWrapper = profileState.value

    val parentUser: ParentUser? by profileStateWrapper.parentUserState.collectAsStateWithLifecycle()
    val currentLanguage: String? by profileStateWrapper.currentLanguageCode.collectAsStateWithLifecycle()


    var country: String by rememberSaveable {
        mutableStateOf(countryList[0].name)
    }

    val mContext = LocalContext.current
    var isExpanded by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()

    Scaffold()
    { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = PrimaryGreen)
                .padding(contentPadding)
        ) {

            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Box(modifier = Modifier) {
                    Icon(
                        tint = Beige, painter =
                        painterResource(id = R.drawable.pclogo),
                        contentDescription = "Aurora Logo",
                        modifier = Modifier.size(130.dp)
                    )
                }

                Spacer(modifier = Modifier.size(30.dp))

                Text(
                    text = stringResource(id = R.string.account_setup),
                    textAlign = TextAlign.Center,
                    lineHeight = 42.sp,
                    fontFamily = PlexSans,
                    fontWeight = FontWeight.Medium,
                    fontSize = 36.sp,
                    color = Beige,
                )

                Spacer(modifier = Modifier.size(30.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {


                    Spacer(modifier = Modifier.size(20.dp))

                    ExposedDropdownMenuBox(

                        expanded = isExpanded,
                        onExpandedChange = {
                            isExpanded = !isExpanded
                        })
                    {

                        TextField(
                            readOnly = true,
                            value = country,
                            onValueChange = {
                                country = it
                            },
                            label = {
                                Text(
                                    text = stringResource(id = R.string.country_label),
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

                            countryList.forEach { countryItem ->
                                DropdownMenuItem(text = { Text(text = countryItem.name) },
                                    onClick = {
                                        country = countryItem.name
                                        isExpanded = false
                                    })
                            }
                        }

                    }
                    Spacer(modifier = Modifier.size(20.dp))

                    Box(
                        modifier = Modifier
                            .width(180.dp)
                            .padding(10.dp)
                            .clip(
                                RoundedCornerShape(30.dp)
                            )
                            .clickable {
                                parentUser?.let { currentParentUser ->
                                    onEvent(
                                        ProfileEvent.UpdateCountry(currentParentUser, country)
                                    )
                                }.also {
                                    isExpanded = false
                                    Toast.makeText(mContext, "Successfully updated Country", Toast.LENGTH_LONG ).show()
                                    navController.navigate(Screen.SettingsHomeScreen.route)
                                }
                            }
                            .background(color = LightGreen)
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