package com.example.parentcoachbot.feature_chat.presentation.emergency_screen

import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parentcoachbot.R
import com.example.parentcoachbot.ui.theme.TextGrey
import com.example.parentcoachbot.ui.theme.ThinGreen


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ExpandableCard(
    @StringRes cardHeaderStringId: Int = R.string.medical_emergency_label,
    content: @Composable () -> Unit = { }, headerFontSize: TextUnit = 27.sp
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            )
            .padding(10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(color = ThinGreen),
        onClick = {
            isExpanded = !isExpanded
        }, shape = Shapes().medium
    ) {

        Column(
            modifier = Modifier
                .background(color = ThinGreen)
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = cardHeaderStringId),
                    fontSize = headerFontSize,
                    color = TextGrey,
                    modifier = Modifier
                        .padding(10.dp)
                        .weight(6f),
                    maxLines = 1,
                    overflow = TextOverflow.Visible
                )

                IconButton(modifier = Modifier.weight(1f), onClick = {
                    isExpanded = !isExpanded
                }) {
                    Icon(
                        imageVector = if (!isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                        contentDescription = null, tint = TextGrey
                    )
                }
            }
            if (isExpanded) {
                content()
            }


        }
    }
}


@Composable
@Preview
fun ExpandableCardPreview() {
    ExpandableCard()
}