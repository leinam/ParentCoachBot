package com.example.parentcoachbot.feature_chat.presentation.profile_screen

import android.widget.Toast
import androidx.annotation.DrawableRes
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.parentcoachbot.feature_chat.domain.model.ChildProfile
import com.example.parentcoachbot.feature_chat.domain.model.ParentUser
import com.example.parentcoachbot.feature_chat.domain.model.Sex
import com.example.parentcoachbot.feature_chat.presentation.Screen
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
fun AddProfileScreen(
    navController: NavController = rememberNavController(),
    onEvent: (profileEvent: ProfileEvent) -> Unit = {},
    profileState: State<ProfileStateWrapper> = mutableStateOf(ProfileStateWrapper())
) {

    val profileStateWrapper = profileState.value

    val parentUser: ParentUser? by profileStateWrapper.parentUserState.collectAsStateWithLifecycle()
    val currentLanguage: String? by profileStateWrapper.currentLanguageCode.collectAsStateWithLifecycle()

    var name: String? by rememberSaveable {
        mutableStateOf(null)
    }

    val years = (2018..2023).map { it.toString() }

    val months: List<Map<String, String>> = listOf(
        mapOf(
            Pair("en", "January"),
            Pair("zu", "uMasingana"),
            Pair("pt", "Janeiro")
        ),
        mapOf(
            Pair("en", "February"),
            Pair("zu", "uNhlolanja"),
            Pair("pt", "Fevereiro")
        ),
        mapOf(
            Pair("en", "March"),
            Pair("zu", "uNdasa"),
            Pair("pt", "Março")
        ),
        mapOf(
            Pair("en", "April"),
            Pair("zu", "uMbasa"),
            Pair("pt", "Abril")
        ),
        mapOf(
            Pair("en", "May"),
            Pair("zu", "uNhlaba"),
            Pair("pt", "Maio")
        ),
        mapOf(
            Pair("en", "June"),
            Pair("zu", "uNhlangulana"),
            Pair("pt", "Junho")
        ),
        mapOf(
            Pair("en", "July"),
            Pair("zu", "uNtulikazi"),
            Pair("pt", "Julho")
        ),
        mapOf(
            Pair("en", "August"),
            Pair("zu", "uNcwaba"),
            Pair("pt", "Agosto")
        ),
        mapOf(
            Pair("en", "September"),
            Pair("zu", "uMandulo"),
            Pair("pt", "Setembro")
        ),
        mapOf(
            Pair("en", "October"),
            Pair("zu", "uMfumfu"),
            Pair("pt", "Outubro")
        ),
        mapOf(
            Pair("en", "November"),
            Pair("zu", "uLwezi"),
            Pair("pt", "Novembro")
        ),
        mapOf(
            Pair("en", "December"),
            Pair("zu", "uZibandlela"),
            Pair("pt", "Dezembro")
        ),

        )

    var yob: String? by rememberSaveable {
        mutableStateOf(null)
    }

    var mob: String? by rememberSaveable {
        mutableStateOf(null)
    }

    var mobIndex: Int? by rememberSaveable {
        mutableStateOf(null)
    }

    var sex: String? by rememberSaveable {
        mutableStateOf(null)
    }

    var pin: String? by rememberSaveable {
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
                    .align(Alignment.Center)
                    .padding(vertical = 20.dp)
                    .verticalScroll(rememberScrollState()),
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
                        .padding(20.dp),
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

                        label = { Text(text = stringResource(id = R.string.profile_name)) },
                        onValueChange = {
                            if (it.length <= 10) name = it.trim() else Toast.makeText(
                                mContext,
                                R.string.toast_warning_name,
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        textStyle = LocalTextStyle.current.copy(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // TODO add date picker


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
                                            if (!mob.isNullOrBlank()) mobIndex?.plus(1) else null
                                        this.yearOfBirth =
                                            if (!yob.isNullOrBlank()) yob?.toInt() else null
                                        this.name = name.toString()
                                        this.parentUser = parentUser?._id
                                    }

                                    onEvent(ProfileEvent.NewProfile(newChildProfile)).also {
                                        onEvent(ProfileEvent.SelectProfile(newChildProfile))

                                        navController.navigate(Screen.ChatListScreen.route)
                                        {
                                            popUpTo(Screen.AddProfileScreen.route) {
                                                inclusive = true
                                            }
                                        }
                                    }
                                } else {
                                    Toast
                                        .makeText(
                                            mContext,
                                            R.string.toast_warning_name,
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AccountSetupScreen(
    navController: NavController = rememberNavController(),
    onEvent: (profileEvent: ProfileEvent) -> Unit = {},
    profileState: State<ProfileStateWrapper> = mutableStateOf(ProfileStateWrapper())
) {

    val profileStateWrapper = profileState.value

    val parentUser: ParentUser? by profileStateWrapper.parentUserState.collectAsStateWithLifecycle()
    val currentLanguage: String? by profileStateWrapper.currentLanguageCode.collectAsStateWithLifecycle()

    var username: String by rememberSaveable {
        mutableStateOf("")
    }

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
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .padding(vertical = 20.dp)
                    .verticalScroll(rememberScrollState()),
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

                    OutlinedTextField(
                        value = username ?: "",
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = BackgroundWhite,
                            focusedContainerColor = BackgroundWhite,
                            focusedLabelColor = BackgroundWhite
                        ),

                        label = { Text(text = stringResource(id = R.string.participant_username_label)) },
                        onValueChange = {
                            if (it.length <= 10) username = it.trim() else Toast.makeText(
                                mContext,
                                R.string.toast_warning_name,
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        textStyle = LocalTextStyle.current.copy(),
                        modifier = Modifier.fillMaxWidth()
                    )

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
                                        ProfileEvent.UpdateUserAccount(
                                            username = username,
                                            country = country,
                                            parentUser = currentParentUser
                                        )
                                    )
                                }.also {
                                    username = ""
                                    isExpanded = false
                                    Toast.makeText(mContext, "Successfully updated your account details", Toast.LENGTH_LONG ).show()
                                    navController.navigate(Screen.SelectProfileScreen.route)
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

@Preview
@Composable
fun PinEntryScreen(
    navController: NavController = rememberNavController(),
    state: PinState = PinState(),
    pinCallbacks: PinCallbacks = PinCallbacksImplementation(),
    profileState: State<ProfileStateWrapper> = mutableStateOf(ProfileStateWrapper())
) {

    val profileStateWrapper = profileState.value
    val appPreferences by profileStateWrapper.appPreferences.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = PrimaryGreen)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.size(100.dp))
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

        Spacer(modifier = Modifier.size(15.dp))

        Text(
            text = stringResource(id = R.string.enter_pin_prompt),
            color = Beige, fontSize = 30.sp
        )

        Spacer(modifier = Modifier.size(30.dp))

        PinBOX(navController = navController,
            appPreferences = appPreferences)


    }


}

sealed class Country(val name: String, @DrawableRes val icon: Int) {
    object SouthAfrica : Country("South Africa", R.drawable.icons8_south_africa_96)
    object Portugal : Country("Portugal", R.drawable.icons8_portugal_96)
}

val countryList = listOf(Country.SouthAfrica, Country.Portugal)



