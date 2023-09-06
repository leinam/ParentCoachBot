package com.example.parentcoachbot.ui.theme

import androidx.annotation.DrawableRes

data class NavBarItem(
    val title:String?,
    @DrawableRes val icon:  Int,
    val route: String?
)

