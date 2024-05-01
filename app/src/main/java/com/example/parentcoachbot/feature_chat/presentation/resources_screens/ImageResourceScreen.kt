package com.example.parentcoachbot.feature_chat.presentation.resources_screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.parentcoachbot.feature_chat.presentation.chat_list.ChatListStateWrapper
import com.example.parentcoachbot.ui.theme.Beige

@Preview
@Composable
fun ImageResourceScreen(
    chatListViewModelState: State<ChatListStateWrapper> = mutableStateOf(ChatListStateWrapper()),
    navController: NavController = rememberNavController()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Beige),
        verticalArrangement = Arrangement.Center
    )
    {
        var scale by remember {
            mutableFloatStateOf(1f)
        }

        var offset by remember {
            mutableStateOf(Offset.Zero)
        }

        val imageID by chatListViewModelState.value.currentImageResourceId.collectAsStateWithLifecycle()

        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth()

        ) {
            val state = rememberTransformableState { zoomChange, panChange, rotationChange ->
                scale = (scale * zoomChange).coerceIn(1f, 5f)

                val extraWidth = (scale - 1) * this.constraints.maxWidth
                val extraHeight = (scale - 1) * this.constraints.maxHeight
                val maxX = extraWidth / 2
                val maxY = extraHeight / 2

                offset = Offset(
                    x = (offset.x + panChange.x).coerceIn(-maxX, maxX),
                    y = (offset.y + panChange.y).coerceIn(-maxY, maxY)
                )

                offset += panChange
            }


                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxWidth()
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                                translationX = offset.x
                                translationY = offset.y
                            }
                        .transformable(state)
                        ,
                    ) {

                            imageID?.let {
                                Image(
                                    painter = painterResource(id = it),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    contentScale = ContentScale.FillWidth,
                                )
                            }

                        }

                    }


        }


    }



