package com.example.parentcoachbot.feature_chat.presentation

import androidx.annotation.DrawableRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.parentcoachbot.R
import com.example.parentcoachbot.ui.theme.TextGrey

@Preview
@Composable
fun ConfirmDeleteDialog(
    @DrawableRes iconId: Int = R.drawable.baseline_delete_24,
    dialogTitle: String = stringResource(id = R.string.delete_chat_label),
    dialogText: String = stringResource(id = R.string.confirm_delete_chat),
    onConfirmation: () -> Unit = {},
    onDismissRequest: () -> Unit = {},

    ) {
    AlertDialog(
        icon = {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = "Example Icon", tint = TextGrey
            )
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(stringResource(id = R.string.delete_label))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(stringResource(id = R.string.cancel_label))
            }
        }
    )
}

