package com.example.parentcoachbot.feature_chat.presentation.chat_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.parentcoachbot.R
import com.example.parentcoachbot.feature_chat.domain.model.Question
import com.example.parentcoachbot.feature_chat.domain.model.QuestionSession
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.ChatEvent
import com.example.parentcoachbot.ui.theme.BackgroundWhite
import com.example.parentcoachbot.ui.theme.LighterOrange
import com.example.parentcoachbot.ui.theme.Orange
import com.example.parentcoachbot.ui.theme.PlexSans
import com.example.parentcoachbot.ui.theme.TextGrey

@Preview
@Composable
fun QuestionBox(
    question: Question =
        Question().apply { this.questionTextEn = "Do I have enough milk?" },
    questionSession: QuestionSession? = null,
    onEvent: (chatEvent: ChatEvent) -> Unit = {},
    currentLanguageCode: String = "en"
) {
    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }

    val questionText =
        if (currentLanguageCode == "pt") question.questionTextPt else if (currentLanguageCode == "zu") question.questionTextZu else question.questionTextEn


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
                .pointerInput(true) {
                    detectTapGestures(onLongPress = {
                        isContextMenuVisible = true
                    })
                }
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
                    questionSession?.let { onEvent(ChatEvent.DeleteQuestionSession(questionSession = it)) }
                    isContextMenuVisible = false
                })

            DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.save_question_label)) },
                onClick = {
                    questionSession?.let { onEvent(ChatEvent.SaveQuestionSession(questionSession._id)) }
                    isContextMenuVisible = false
                })

        }
    }


}