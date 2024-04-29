package com.example.parentcoachbot.feature_chat.presentation.resources_screens

import android.content.Context
import android.content.res.AssetManager
import android.util.DisplayMetrics
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.parentcoachbot.feature_chat.domain.util.PdfRender
import com.example.parentcoachbot.ui.theme.Beige

@Preview
@Composable
fun PDFReader(
    fileName: String = "rthb_booklet.pdf",
    navController: NavController = rememberNavController()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Beige)
    )
    {
        var scale by remember {
            mutableStateOf(1f)
        }

        var offset by remember {
            mutableStateOf(Offset.Zero)
        }

        val assetManager: AssetManager = LocalContext.current.assets


        val pdfRender = PdfRender(
            inputStream = assetManager.open(fileName),
            context = LocalContext.current,
            fileName = fileName
        )

        DisposableEffect(key1 = Unit) {
            onDispose {
                pdfRender.close()
            }
        }

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

            LazyColumn {
                items(count = pdfRender.pageCount) { index ->
                    BoxWithConstraints(
                        modifier = Modifier.fillMaxWidth()
                            .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            translationX = offset.x
                            translationY = offset.y
                        }
                            //.transformable(state),
                    ) {


                        val page = pdfRender.pageLists[index]
                        DisposableEffect(key1 = Unit) {
                            page.load()
                            onDispose {
                                page.recycle()
                            }
                        }

                        page.pageContent.collectAsState().value?.asImageBitmap()?.let {

                            Image(
                                bitmap = it,
                                contentDescription = "Pdf page number: $index",
                                modifier = Modifier
                                    .fillMaxWidth(),
                                contentScale = ContentScale.FillWidth
                            )
                        }
                    }
                }
            }
        }


    }
}

fun Int.pxToDp(context: Context): Float =
    (this / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT))

