package com.example.parentcoachbot.feature_chat.presentation.profile_screen

data class PinState(val pin: String = "1234", val pinButtonEnabled: Boolean = true, val pinError: Boolean = false)

interface PinCallbacks {
    fun onPinChange(pin: String)
    fun onPinUnlockClick(pin: String): Boolean
}

class PinCallbacksImplementation(): PinCallbacks{
    override fun onPinChange(pin: String) {

    }

    override fun onPinUnlockClick(pin: String): Boolean {
        return pin == "4785"

    }

}