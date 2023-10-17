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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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

    val chatStateWrapper = chatViewModelState.value
    val application by chatStateWrapper.application.collectAsStateWithLifecycle()
    val context = application?.applicationContext

    Scaffold()
    { contentPadding ->
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
                    items(languageList) { item ->
                        Box(modifier = Modifier
                            .padding(10.dp)
                            .clickable {
                                onEvent(ChatEvent.ChangeLanguage(item.isoCode)).also {
                                    val intent = Intent(context, MainActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    context?.startActivity(intent)
                                }
                            }
                            .clip(RoundedCornerShape(10.dp))
                            .background(color = ThinGreen)
                            .fillMaxWidth()
                            .height(70.dp)
                            .padding(10.dp)

                        )
                        {

                            Row(
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {


                                Row {
                                    Image(
                                        painter = painterResource(id = item.icon),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(end = 18.dp)
                                            .align(Alignment.CenterVertically)
                                    )

                                    item.name.let {
                                        Text(
                                            text = stringResource(id = it),
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


            }

        }

    }


}

val languageList: List<Language> = listOf(Language.English, Language.Portuguese, Language.Zulu)
