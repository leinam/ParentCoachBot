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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parentcoachbot.R
import com.example.parentcoachbot.feature_chat.domain.model.Answer
import com.example.parentcoachbot.ui.theme.BackgroundWhite
import com.example.parentcoachbot.ui.theme.LightBeige
import com.example.parentcoachbot.ui.theme.PlexSans
import com.example.parentcoachbot.ui.theme.PrimaryGreen

@Preview
@Composable
fun AnswerBox(modifier: Modifier = Modifier,
              questionAnswer: Answer = Answer(),
              cornerRadius: Dp = 20.dp) {

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
                        topEnd = cornerRadius,
                        bottomStart = cornerRadius,
                        bottomEnd = cornerRadius
                    )
                )
                .background(color = LightBeige)
                .padding(16.dp)
                .align(Alignment.Center)

        ) {
            Text(text = questionAnswer.answerTextPt ?: "",
                textAlign = TextAlign.Start,
                fontFamily = PlexSans,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp)
        }

        Icon(
            painter = painterResource(id = R.drawable.support_agent),
            contentDescription = null,
            modifier = Modifier
                .padding(10.dp)
                .clip(CircleShape)
                .background(color = PrimaryGreen)
                .padding(4.dp)
                .align(Alignment.TopStart),
            tint = BackgroundWhite
        )
    }
}