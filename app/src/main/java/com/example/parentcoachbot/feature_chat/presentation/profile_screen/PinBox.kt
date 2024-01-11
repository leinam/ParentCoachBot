package com.example.parentcoachbot.feature_chat.presentation.profile_screen

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.parentcoachbot.feature_chat.presentation.Screen
import com.example.parentcoachbot.ui.theme.TextGrey

@Preview
@Composable
fun PinBOX(
    pinCallbacks: PinCallbacks = PinCallbacksImplementation(),
    navController: NavController = rememberNavController()
) {
    var pin by remember {
        mutableStateOf("")
    }

    var passwordVisible by rememberSaveable {
        mutableStateOf(false)
    }

    val context = LocalContext.current
    

    BasicTextField(
        value = pin,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        onValueChange = {
            pin = it
            if (pin.length == 4) {

                val success = pinCallbacks.onPinUnlockClick(pin)

                if (success) {
                    navController.navigate(route = Screen.SelectProfileScreen.route)
                } else {
                    Toast.makeText(context, "Wrong Pin. Try Again", Toast.LENGTH_SHORT).show()
                    pin = ""
                }
            }
        },
        decorationBox = { innerTextField ->
            Row(horizontalArrangement = Arrangement.Center) {
                repeat(4) { index ->
                    val isFocused = index == pin.length
                    val char = when {
                        index >= pin.length -> ""
                        else -> pin[index].toString()
                    }


                    DigitBox(char, isFocused = isFocused,
                        passwordVisible = passwordVisible)

                    Spacer(modifier = Modifier.width(8.dp))

                }
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
    )


}

@Preview
@Composable
fun DigitBox(
    digit: String = "1",
    isFocused: Boolean = true,
    passwordVisible: Boolean = false
) {
    Text(
        text = if (passwordVisible) digit else if (digit.isNotBlank()) "*" else "",
        modifier = Modifier
            .width(50.dp).height(50.dp)
            .border(
                width = 1.dp,
                color = if (isFocused) Color.Black else TextGrey,
                shape = RoundedCornerShape(8.dp)
            ),
        style = MaterialTheme.typography.headlineSmall,
        color = Color.DarkGray,
        textAlign = TextAlign.Center
    )
}