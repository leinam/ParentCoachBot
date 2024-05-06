package com.example.parentcoachbot.feature_chat.presentation.chat_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parentcoachbot.R
import com.example.parentcoachbot.feature_chat.domain.model.Question
import com.example.parentcoachbot.feature_chat.domain.util.Language
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.ChatEvent
import com.example.parentcoachbot.ui.theme.BackgroundBeige
import com.example.parentcoachbot.ui.theme.Beige
import com.example.parentcoachbot.ui.theme.LightGreen
import com.example.parentcoachbot.ui.theme.PlexSans
import com.example.parentcoachbot.ui.theme.PrimaryGreen
import com.example.parentcoachbot.ui.theme.TextGrey
import io.realm.kotlin.ext.realmDictionaryOf

@Preview
@Composable
fun RelatedQuestionsBox(
    modifier: Modifier = Modifier,
    previousQuestion: Question? = null,
    relatedQuestionsList: List<Question?> = listOf(Question().apply {
        this.questionText = realmDictionaryOf(Pair("en", "Do I need to breastfeed?"))
    }, Question().apply {
        this.questionText = realmDictionaryOf(Pair("en", "Do I need to be there?"))
    }),
    onEvent: (ChatEvent) -> Unit = {},
    cornerRadius: Dp = 20.dp,
    currentLanguageCode: String = Language.English.isoCode
) {


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 10.dp,
                bottom = 10.dp
            )
    )
    {
        Column(
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
                .background(color = PrimaryGreen)
                .padding(16.dp)
                .align(Alignment.Center)

        ) {

            Text(
                text = stringResource(id = R.string.related_questions_label),
                textAlign = TextAlign.Start,
                fontFamily = PlexSans,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            relatedQuestionsList.forEach {
                it?.let { question ->
                    Box(
                        Modifier
                            .width(240.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(color = BackgroundBeige)
                            .padding(8.dp)
                            .clickable {
                                onEvent(
                                    ChatEvent.AddQuestionSession(
                                        question,
                                        previousQuestion = previousQuestion
                                    )
                                )
                            }
                    ) {
                        question.questionText.let { questionText ->
                            Text(
                                text = questionText[currentLanguageCode]
                                    ?: questionText["en"] ?: "",
                                textAlign = TextAlign.Start,
                                fontFamily = PlexSans,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp, color = TextGrey
                            )
                        }


                    }
                    Spacer(Modifier.size(8.dp))
                }

            }

        }

        Icon(
            painter = painterResource(id = R.drawable.robot),
            contentDescription = null,
            modifier = Modifier
                .size(54.dp)
                .padding(10.dp)
                .clip(CircleShape)
                .background(color = LightGreen)
                .padding(4.dp)
                .align(Alignment.TopStart),
            tint = Beige
        )
    }
}