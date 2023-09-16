package com.example.parentcoachbot.feature_chat.presentation.chat_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.parentcoachbot.R
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.ChatEvent
import com.example.parentcoachbot.ui.theme.BackgroundWhite
import com.example.parentcoachbot.ui.theme.DarkGrey
import com.example.parentcoachbot.ui.theme.PrimaryGreen

@Preview
@Composable
fun QuestionInputSection(modifier: Modifier = Modifier,
                         placeholderText: String = "Ask me a Question",
                         onEvent: (chatEvent: ChatEvent) -> Unit = {}) {

    var searchQueryText by remember { mutableStateOf(TextFieldValue("")) }

    Row(modifier = modifier,
        horizontalArrangement = Arrangement.SpaceAround)
    {
        TextField(modifier = Modifier
            .clip(RoundedCornerShape(30.dp))
            , value = searchQueryText,
            onValueChange = {
                searchQueryText = it
                onEvent(ChatEvent.UpdateSearchQueryText(it.text))
            },

            colors = TextFieldDefaults.colors(
                focusedContainerColor = BackgroundWhite,
                unfocusedContainerColor = BackgroundWhite,
                disabledContainerColor = BackgroundWhite,
                focusedTextColor = Color.Black,
                unfocusedTextColor = DarkGrey
            ), placeholder = { Text(text = placeholderText) })
        Box(modifier = Modifier
            .clip(CircleShape)
            .background(color = PrimaryGreen)
            .padding(5.dp)
            .size(40.dp)
            .clickable {

            },
            contentAlignment = Alignment.Center
        ){
            Icon(painter = painterResource(id = R.drawable.baseline_send_24),
                contentDescription = "Send")
        }

    }
}