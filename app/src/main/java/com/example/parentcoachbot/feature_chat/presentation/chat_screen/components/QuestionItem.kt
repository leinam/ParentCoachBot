package com.example.parentcoachbot.feature_chat.presentation.chat_screen.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.example.parentcoachbot.R
import com.example.parentcoachbot.feature_chat.domain.model.Question
import com.example.parentcoachbot.feature_chat.domain.model.QuestionSession
import com.example.parentcoachbot.feature_chat.domain.util.Language
import com.example.parentcoachbot.feature_chat.presentation.ConfirmDeleteDialog
import com.example.parentcoachbot.feature_chat.presentation.Screen
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.ChatEvent
import com.example.parentcoachbot.feature_chat.presentation.saved_questions_screen.SavedQuestionsScreenEvent
import com.example.parentcoachbot.ui.theme.BackgroundWhite
import com.example.parentcoachbot.ui.theme.LighterOrange
import com.example.parentcoachbot.ui.theme.Orange
import com.example.parentcoachbot.ui.theme.PlexSans
import com.example.parentcoachbot.ui.theme.TextGrey
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun QuestionBox(
    question: Question =
        Question().apply { this.questionText["en"] = "Do I have enough milk?" },
    questionSession: QuestionSession? = QuestionSession().apply { isSaved = true },
    onEvent: (chatEvent: ChatEvent) -> Unit = {},
    screenName: String = Screen.ChatScreen.route,
    onSavedScreenEvent: (savedQuestionsScreenEvent: SavedQuestionsScreenEvent) -> Unit = {},
    currentLanguageCode: String = Language.English.isoCode,
    toggleAnswerVisibility: () -> Unit = {},
    openAlertDialogState: MutableState<Boolean> = mutableStateOf(false)
) {
    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }

    when {
        openAlertDialogState.value -> {
            ConfirmDeleteDialog(
                onConfirmation = {
                    openAlertDialogState.value = false
                    questionSession?.let {
                        onEvent(ChatEvent.DeleteQuestionSession(questionSession = it))
                    }
                },
                dialogText = stringResource(id = R.string.confirm_delete_question),
                dialogTitle = stringResource(id = R.string.delete_question_label),
                onDismissRequest = {
                    openAlertDialogState.value = false
                })
        }
    }

    val questionText = question.questionText[currentLanguageCode]

    var timeAsked = ""


    questionSession?.timeAsked?.let {
        timeAsked = LocalDateTime.ofEpochSecond(
            it.epochSeconds,
            it.nanosecondsOfSecond,
            ZoneOffset.UTC
        ).format(DateTimeFormatter.ofPattern("H:mm"))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 10.dp,
                bottom = 10.dp
            )
    )
    {
        Box(
            modifier = Modifier
                .width(300.dp)
                .padding(10.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 20.dp,
                        bottomStart = 20.dp,
                        bottomEnd = 20.dp
                    )
                )

                .combinedClickable(
                    onLongClick = { isContextMenuVisible = true },
                    onClick = { toggleAnswerVisibility() })
                .background(color = LighterOrange)
                .padding(16.dp)
                .align(Alignment.Center)
        ) {
            questionText?.let {
                Text(
                    text = it,
                    textAlign = TextAlign.Start,
                    fontFamily = PlexSans,
                    fontWeight = FontWeight.Normal,
                    color = TextGrey
                )
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                if (questionSession?.isSaved == true) {
                    Icon(
                        painter = painterResource(id = R.drawable.save_button_filled),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .size(12.dp),
                        tint = TextGrey
                    )
                }

                Text(
                    text = timeAsked,
                    fontSize = 11.sp, modifier = Modifier.padding(start = 5.dp, top = 10.dp),
                    color = TextGrey
                )
            }


        }

        Icon(
            painter = painterResource(id = R.drawable.baseline_face_6_24),
            contentDescription = null,
            modifier = Modifier
                .padding(10.dp)
                .clip(CircleShape)
                .background(color = Orange)
                .padding(4.dp)
                .align(Alignment.TopEnd),
            tint = BackgroundWhite
        )
        DropdownMenu(
            expanded = isContextMenuVisible,
            onDismissRequest = { isContextMenuVisible = false },
            modifier = Modifier.padding(10.dp)
        ) {
            DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.delete_question_label)) },
                onClick = {
                    questionSession?.let {
                        if (screenName == Screen.ChatScreen.route) {
                            openAlertDialogState.value = true
                            isContextMenuVisible = false

                        } else if (screenName == Screen.SavedQuestionsScreen.route) {
                            onSavedScreenEvent(
                                SavedQuestionsScreenEvent.DeleteQuestionSession(
                                    questionSession = it
                                )
                            )

                        }
                    }
                    isContextMenuVisible = false
                })

            DropdownMenuItem(
                text = { Text(text = stringResource(id = if (questionSession?.isSaved == true) R.string.unsave_question_label else R.string.save_question_label)) },
                onClick = {
                    questionSession?.let {
                        if (screenName == Screen.ChatScreen.route) {
                            onEvent(ChatEvent.SaveQuestionSession(questionSession._id))
                        } else if (screenName == Screen.SavedQuestionsScreen.route) {
                            onSavedScreenEvent(
                                SavedQuestionsScreenEvent.SaveQuestionSession(
                                    questionSession._id
                                )
                            )
                        }
                    }
                    isContextMenuVisible = false
                })

        }
    }


}