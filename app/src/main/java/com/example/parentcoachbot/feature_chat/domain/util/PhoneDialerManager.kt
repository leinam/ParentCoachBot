package com.example.parentcoachbot
    .feature_chat.domain.util

import android.content.Context
import android.content.Intent
import android.net.Uri

class PhoneDialerManager {

    companion object{
        fun openDialPad(context: Context, phoneNum: String) {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:$phoneNum"))
            context.startActivity(intent)
        }
    }

}