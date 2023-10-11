package com.example.parentcoachbot.feature_chat.presentation.profile_screen

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
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.example.parentcoachbot.feature_chat.domain.model.ChildProfile
import com.example.parentcoachbot.feature_chat.domain.model.ParentUser
import com.example.parentcoachbot.feature_chat.domain.model.Sex
import com.example.parentcoachbot.feature_chat.presentation.Screen
import com.example.parentcoachbot.ui.theme.BackgroundWhite
import com.example.parentcoachbot.ui.theme.Beige
import com.example.parentcoachbot.ui.theme.LightGreen
import com.example.parentcoachbot.ui.theme.PlexSans
import com.example.parentcoachbot.ui.theme.PrimaryGreen


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AddProfileScreen(
    navController: NavController = rememberNavController(),
    onEvent: (profileEvent: ProfileEvent) -> Unit = {},
    profileState: State<ProfileStateWrapper> = mutableStateOf(ProfileStateWrapper())
) {

    val profileStateWrapper = profileState.value

    val parentUser: ParentUser? by profileStateWrapper.parentUserState.collectAsStateWithLifecycle()

    var name: String? by rememberSaveable {
        mutableStateOf(null)
    }

    val years = (2018..2023).map { it.toString() }

    val months = listOf(
        "January", "February", "March", "April", "May", "June", "July",
        "August", "September", "October", "November", "December"
    )

    var yob: String? by rememberSaveable {
        mutableStateOf(null)
    }

    var mob: String? by rememberSaveable {
        mutableStateOf(null)
    }

    var sex: String? by rememberSaveable {
        mutableStateOf(null)
    }

    var isGenderDropdownExpanded by remember {
        mutableStateOf(false)
    }

    var isMobDropdownExpanded by remember {
        mutableStateOf(false)
    }

    var isYobDropdownExpanded by remember {
        mutableStateOf(false)
    }

    val mContext = LocalContext.current

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
                    .align(Alignment.TopCenter)
                    .padding(vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Box(modifier = Modifier) {
                    Icon(
                        tint = Beige, painter =
                        painterResource(id = R.drawable.logo_sleepy),
                        contentDescription = "Aurora Logo"
                    )
                }

                Spacer(modifier = Modifier.size(30.dp))

                Text(
                    text = stringResource(id = R.string.create_profile_header),
                    textAlign = TextAlign.Center,
                    fontFamily = PlexSans,
                    fontWeight = FontWeight.Medium,
                    fontSize = 36.sp,
                    color = Beige
                )

                Spacer(modifier = Modifier.size(30.dp))


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    OutlinedTextField(
                        value = name ?: "",
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = BackgroundWhite,
                            focusedContainerColor = BackgroundWhite,
                            focusedLabelColor = BackgroundWhite
                        ),

                        label = { Text(text = stringResource(id = R.string.child_name_label)) },
                        placeholder = { Text(text = stringResource(id = R.string.name_placeholder)) },
                        onValueChange = {
                            if (it.length <= 10) name = it.trim() else Toast.makeText(
                                mContext,
                                R.string.toast_warning_name,
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        textStyle = LocalTextStyle.current.copy()
                    )

                    // TODO add date picker

                    ExposedDropdownMenuBox(
                        expanded = isGenderDropdownExpanded,
                        onExpandedChange = {
                            isGenderDropdownExpanded = !isGenderDropdownExpanded
                        },
                    ) {
                        OutlinedTextField(
                            value = sex ?: "",
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = BackgroundWhite,
                                focusedContainerColor = BackgroundWhite,
                                focusedLabelColor = BackgroundWhite
                            ),
                            modifier = Modifier.menuAnchor(),
                            label = { Text(text = stringResource(R.string.child_gender_label)) },
                            placeholder = { Text(text = stringResource(id = R.string.gender_placeholder)) },
                            readOnly = true,
                            onValueChange = {
                                sex = it
                                isGenderDropdownExpanded = false
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = isGenderDropdownExpanded
                                )
                            },
                        )

                        ExposedDropdownMenu(expanded = isGenderDropdownExpanded,
                            onDismissRequest = { isGenderDropdownExpanded = false }) {
                            DropdownMenuItem(
                                text = {
                                    Text(text = Sex.Male.name)
                                },
                                onClick = {
                                    sex = Sex.Male.name
                                    isGenderDropdownExpanded = false
                                }
                            )

                            DropdownMenuItem(
                                text = {
                                    Text(text = Sex.Female.description)
                                },
                                onClick = {
                                    sex = Sex.Female.name
                                    isGenderDropdownExpanded = false
                                }
                            )

                            DropdownMenuItem(
                                text = {
                                    Text(text = Sex.NotSpecified.description)
                                },
                                onClick = {
                                    sex = Sex.NotSpecified.name
                                    isGenderDropdownExpanded = false
                                }
                            )

                        }

                    }

                    ExposedDropdownMenuBox(
                        expanded = isMobDropdownExpanded,
                        onExpandedChange = { isMobDropdownExpanded = !isMobDropdownExpanded },
                    ) {
                        OutlinedTextField(
                            value = mob ?: "",
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = BackgroundWhite,
                                focusedContainerColor = BackgroundWhite,
                                focusedLabelColor = BackgroundWhite
                            ),
                            modifier = Modifier.menuAnchor(),
                            label = { Text(text = stringResource(R.string.month_placeholder)) },
                            placeholder = { Text(text = stringResource(id = R.string.month_label)) },
                            readOnly = true,
                            onValueChange = {
                                mob = it
                                isMobDropdownExpanded = false
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = isMobDropdownExpanded
                                )
                            },
                        )

                        ExposedDropdownMenu(expanded = isMobDropdownExpanded,
                            onDismissRequest = { isMobDropdownExpanded = false }) {
                            months.forEachIndexed { index, month ->
                                DropdownMenuItem(
                                    text = { Text(text = month) },
                                    onClick = {
                                        mob = month
                                        isMobDropdownExpanded = false
                                    })

                            }
                        }
                    }

                    ExposedDropdownMenuBox(
                        expanded = isYobDropdownExpanded,
                        onExpandedChange = {
                            isYobDropdownExpanded = !isYobDropdownExpanded
                        },
                    ) {
                        OutlinedTextField(
                            value = yob ?: "",
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = BackgroundWhite,
                                focusedContainerColor = BackgroundWhite,
                                focusedLabelColor = BackgroundWhite
                            ),
                            modifier = Modifier
                                .menuAnchor(),
                            label = { Text(text = stringResource(R.string.year_placeholder)) },
                            placeholder = { Text(text = stringResource(id = R.string.year_label)) },
                            readOnly = true,
                            onValueChange = {
                                yob = it
                                isYobDropdownExpanded = false
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = isYobDropdownExpanded
                                )
                            },
                        )

                        ExposedDropdownMenu(expanded = isYobDropdownExpanded,
                            onDismissRequest = { isYobDropdownExpanded = false }) {

                            years.forEach { year ->
                                DropdownMenuItem(
                                    text = { Text(text = year) },
                                    onClick = {
                                        yob = year
                                        isYobDropdownExpanded = false
                                    })

                            }
                        }
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
                                if (!name.isNullOrBlank()) {
                                    val newChildProfile = ChildProfile().apply {
                                        this.gender =
                                            if (!sex.isNullOrBlank()) sex.toString() else Sex.NotSpecified.name
                                        this.monthOfBirth =
                                            if (!mob.isNullOrBlank()) months.indexOf(mob) + 1 else null
                                        this.yearOfBirth =
                                            if (!yob.isNullOrBlank()) yob?.toInt() else null
                                        this.name = name.toString()
                                        this.parentUser = parentUser?._id
                                    }

                                    onEvent(ProfileEvent.newProfile(newChildProfile)).also {
                                        navController.navigate(Screen.ChatListScreen.route)
                                        {
                                            popUpTo(Screen.AddProfileScreen.route) {
                                                inclusive = true
                                            }
                                        }
                                    }
                                }

                            }
                            .background(color = LightGreen)
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.create_profile_button_label).uppercase(),
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
