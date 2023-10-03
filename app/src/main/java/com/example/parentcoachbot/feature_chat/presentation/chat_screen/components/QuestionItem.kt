package com.example.parentcoachbot.feature_chat.presentation.chat_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.parentcoachbot.R
import com.example.parentcoachbot.feature_chat.domain.model.Question
import com.example.parentcoachbot.ui.theme.BackgroundWhite
import com.example.parentcoachbot.ui.theme.LighterOrange
import com.example.parentcoachbot.ui.theme.Orange
import com.example.parentcoachbot.ui.theme.PlexSans
import com.example.parentcoachbot.ui.theme.TextGrey

@Preview
@Composable
fun QuestionBox(question: Question =
                    Question().apply { this.questionTextEn = "Do I have enough milk?" }) {

    Box(modifier = Modifier
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
                .background(color = LighterOrange)
                .padding(16.dp)
                .align(Alignment.Center)
        ) {
            question.questionTextEn?.let {
                Text(text = it,
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
    }
}