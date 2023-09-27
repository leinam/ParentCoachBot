package com.example.parentcoachbot.feature_chat.presentation.chat_screen.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.parentcoachbot.R
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.BottomSheetContent
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.ChatEvent
import com.example.parentcoachbot.ui.theme.BackgroundWhite
import com.example.parentcoachbot.ui.theme.TextGrey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun QuestionInputSection(
    modifier: Modifier = Modifier,
    @StringRes placeholderText: Int = R.string.search_bar_placeholder,
    onEvent: (chatEvent: ChatEvent) -> Unit = {},
    bottomSheetContentState: MutableStateFlow<BottomSheetContent> = MutableStateFlow(
        BottomSheetContent.Topics
    ),
    bottomSheetScaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(),
    scope: CoroutineScope = rememberCoroutineScope()
) {

    var searchQueryText by remember { mutableStateOf(TextFieldValue("")) }

    Row(
        modifier = modifier
    )
    {
        TextField(modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(30.dp))
            .onFocusChanged {
                if (it.isCaptured) {
                    scope.launch {
                        bottomSheetScaffoldState.bottomSheetState.expand()
                    }
                } else {
                    scope.launch {
                        bottomSheetScaffoldState.bottomSheetState.partialExpand()
                    }
                }
            },
            value = searchQueryText,
            onValueChange = {
                scope.launch {
                    bottomSheetScaffoldState.bottomSheetState.expand()
                }
                searchQueryText = it
                onEvent(ChatEvent.UpdateSearchQueryText(it.text))
                bottomSheetContentState.value = BottomSheetContent.SearchResults

            },

            colors = TextFieldDefaults.colors(
                focusedContainerColor = BackgroundWhite,
                unfocusedContainerColor = BackgroundWhite,
                disabledContainerColor = BackgroundWhite,
                focusedTextColor = Color.Black,
                unfocusedTextColor = TextGrey
            ),
            trailingIcon = { Icon(painter = painterResource(id = R.drawable.baseline_search_24), contentDescription = null)},

            placeholder = { Text(text = stringResource (placeholderText)) })
    }
}