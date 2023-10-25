package com.example.parentcoachbot.feature_chat.presentation.chat_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parentcoachbot.feature_chat.domain.model.Topic
import com.example.parentcoachbot.feature_chat.domain.util.Language
import com.example.parentcoachbot.ui.theme.DeepBlue
import com.example.parentcoachbot.ui.theme.LightBlue
import com.example.parentcoachbot.ui.theme.PlexSans

@Composable
fun KnowMoreBox(relatedTopics: List<Topic>,
                currentLanguage: String = Language.English.isoCode
) {
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
                        topEnd = 20.dp,
                        bottomStart = 20.dp,
                        bottomEnd = 20.dp
                    )
                )
                .background(color = LightBlue)
                .padding(16.dp)
                .align(Alignment.Center)
        ) {
            Column(){
                Text(text = "Know More About:",
                    textAlign = TextAlign.Start,
                    fontFamily = PlexSans,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp)

                Column() {
                    relatedTopics.forEach { relatedTopic ->
                        Box(
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(15.dp))
                                .background(color = DeepBlue)
                                .padding(15.dp)
                                .clickable { },
                            contentAlignment = Alignment.Center
                        )

                        {


                            Text(
                                text = relatedTopic.title[currentLanguage] ?: "",
                                textAlign = TextAlign.Center,
                                fontFamily = PlexSans,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        }
                    }
                }


            }
        }


    }
}