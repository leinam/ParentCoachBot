package com.example.parentcoachbot.feature_chat.presentation.chat_screen.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.parentcoachbot.ui.theme.PrimaryGreen
import com.example.parentcoachbot.ui.theme.ThinGreen

@Preview
@Composable
fun TypingAnimation(
    modifier: Modifier = Modifier,
    circleSize: Dp = 18.dp,
    circleColor: Color = PrimaryGreen,
    circleSpacing: Dp = 10.dp,
    travelDistance: Dp = 20.dp,
    isAnimationActive: MutableState<Boolean> = mutableStateOf(false)
) {

    val animationActive by remember {
        isAnimationActive
    }

    val transition = rememberInfiniteTransition()

    if (animationActive){
        Row(modifier = modifier) {
            (0..2).forEach { circleIndex ->

                val animatedColor = transition.animateColor(
                    initialValue = ThinGreen,
                    targetValue = PrimaryGreen,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    )
                )


                Box(
                    modifier = Modifier
                        .size(circleSize)
                        .padding(2.dp)
                        .background(
                            shape = CircleShape,
                            color = animatedColor.value
                        )
                ) {

                }
            }
        }
    }else{
        Row(modifier = modifier) {
                Box(
                    modifier = Modifier
                        .size(circleSize)
                        .padding(2.dp)
                        .background(
                            shape = CircleShape,
                            color = PrimaryGreen
                        )
                ) {

                }
            }
        }
    }



