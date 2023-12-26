package com.example.parentcoachbot.feature_chat.presentation.chat_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parentcoachbot.R
import com.example.parentcoachbot.ui.theme.LighterGreen
import com.example.parentcoachbot.ui.theme.TextGrey
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Preview
@Composable
fun DateBox(date: LocalDate = LocalDate.now().minusDays(1),
            currentLanguageCode: String = "en") {

    val displayText: String = when (date) {
        LocalDate.now() -> stringResource(id = R.string.today)
        LocalDate.now()
            .minusDays(1) -> stringResource(id = R.string.yesterday)
        else -> date.format(DateTimeFormatter.ofPattern("MMMM dd", Locale.forLanguageTag(currentLanguageCode)))
    }

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .padding(5.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(color = LighterGreen)
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {

            Text(text = displayText, color = TextGrey, fontSize = 12.sp)
        }
    }

}