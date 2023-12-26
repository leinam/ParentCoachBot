package com.example.parentcoachbot.feature_chat.presentation.settings_screen

import android.content.Intent
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.parentcoachbot.MainActivity
import com.example.parentcoachbot.R
import com.example.parentcoachbot.feature_chat.domain.util.Language
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.ChatEvent
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.ChatStateWrapper
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.CustomNavigationDrawer
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.TopNavBar
import com.example.parentcoachbot.ui.theme.Beige
import com.example.parentcoachbot.ui.theme.PlexSans
import com.example.parentcoachbot.ui.theme.PrimaryGreen
import com.example.parentcoachbot.ui.theme.TextGrey
import com.example.parentcoachbot.ui.theme.ThinGreen

@Preview
@Composable
fun SelectLanguageScreen(
    navController: NavController = rememberNavController(),
    onEvent: (chatEvent: ChatEvent) -> Unit = {},
    chatViewModelState: State<ChatStateWrapper> = mutableStateOf(
        ChatStateWrapper()
    )
) {
    val currentLanguageCode =
        chatViewModelState.value.currentLanguageCode.collectAsStateWithLifecycle()
    val chatStateWrapper = chatViewModelState.value
    val application by chatStateWrapper.application.collectAsStateWithLifecycle()
    val context = application?.applicationContext
    val radioButtonSelectedOption by remember {
        mutableStateOf(currentLanguageCode)
    }

    val scope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()
    val drawerSelectedItemIndex = rememberSaveable { mutableIntStateOf(3) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    Scaffold(topBar = {
        TopNavBar(
            navController = navController,
            screenIndex = 6,
            drawerState = drawerState,
            scope = scope
        )
    })


    { contentPadding ->
        CustomNavigationDrawer(
            drawerState = drawerState,
            drawerSelectedItemIndex = drawerSelectedItemIndex,
            navController = navController,
            contentPadding = contentPadding,
            content ={
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
                                text = stringResource(id = R.string.language_label),
                                color = PrimaryGreen,
                                fontFamily = PlexSans,
                                fontWeight = FontWeight.Normal,
                                fontSize = 19.sp
                            )


                        }

                        LazyColumn {
                            items(languageList) { language ->
                                Box(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(color = ThinGreen)
                                        .fillMaxWidth()
                                        .height(70.dp)
                                        .padding(10.dp)
                                        .clickable {
                                            onEvent(ChatEvent.ChangeLanguage(language.isoCode)).also {
                                                val intent =
                                                    Intent(context, MainActivity::class.java)
                                                intent.flags =
                                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                context?.startActivity(intent)
                                            }
                                        }
                                )
                                {

                                    Row(
                                        modifier = Modifier
                                            .align(Alignment.CenterStart)
                                            .fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(selected = radioButtonSelectedOption.value == language.isoCode,
                                            onClick = {
                                                onEvent(ChatEvent.ChangeLanguage(language.isoCode)).also {
                                                    val intent =
                                                        Intent(context, MainActivity::class.java)
                                                    intent.flags =
                                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    context?.startActivity(intent)
                                                }
                                            })

                                        Row {
                                            Image(
                                                painter = painterResource(id = language.icon),
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .padding(end = 18.dp)
                                                    .align(Alignment.CenterVertically)
                                            )

                                            Text(
                                                text = stringResource(id = language.name),
                                                fontSize = 18.sp,
                                                fontFamily = PlexSans,
                                                fontWeight = FontWeight.SemiBold,
                                                color = TextGrey,
                                                modifier = Modifier.align(
                                                    Alignment.CenterVertically
                                                )
                                            )
                                        }


                                    }


                                }
                            }
                        }


                    }

                }})


    }
}


val languageList: List<Language> = listOf(
    Language.English,
    Language.Portuguese,
    Language.Zulu
)


