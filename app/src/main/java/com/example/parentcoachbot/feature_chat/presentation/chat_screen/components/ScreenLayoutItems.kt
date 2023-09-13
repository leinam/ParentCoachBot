package com.example.parentcoachbot.feature_chat.presentation.chat_screen.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.parentcoachbot.R
import com.example.parentcoachbot.feature_chat.presentation.Screen
import com.example.parentcoachbot.ui.theme.Beige
import com.example.parentcoachbot.ui.theme.DarkGrey
import com.example.parentcoachbot.ui.theme.LightBeige
import com.example.parentcoachbot.ui.theme.LightGreen
import com.example.parentcoachbot.ui.theme.NavBarItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Preview
@Composable
fun TopNavBar(navBarItems: List<NavBarItem>? = listOf(
    NavBarItem("Chats",
        R.drawable.chats_icon,
        route = Screen.ChatListScreen.route),

    NavBarItem("Help",
        R.drawable.help_icon,
        route = Screen.EmergencyInfoScreen.route),

    NavBarItem("Saved",
        R.drawable.favourites_icon,
        route = Screen.SavedQuestionsScreen.route),

    NavBarItem("Profile",
        R.drawable.profile_icon,
        route = Screen.SelectProfileScreen.route)
),
              drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
              scope: CoroutineScope = rememberCoroutineScope(),
              navController: NavController = rememberNavController(),
              screenIndex: Int = 5,
)
{
    var selectedIndex: Int by remember {
        mutableIntStateOf(screenIndex)
    }

    NavigationBar(
        containerColor = LightGreen,
        contentColor = Beige,
        windowInsets = NavigationBarDefaults.windowInsets,
    ) {

        val menuItem = NavBarItem(null,
            R.drawable.menu_icon,
            route = null)

        NavigationBarItem(
            colors=NavigationBarItemDefaults.colors(
            selectedIconColor = DarkGrey,
            indicatorColor = LightBeige)
            ,
            selected = false,
            onClick = {scope.launch {
                drawerState.open()
            }},
            modifier = Modifier
                .padding(vertical = 9.dp),
            label ={
                Text(
                text = "Menu",
                color = Beige,
                style = MaterialTheme.typography.labelSmall)

                    }
                ,
            icon = {
                Icon(
                    painter = painterResource(id = menuItem.icon),
                    contentDescription = menuItem.title,
                    tint = Beige,
                    modifier = Modifier.size(34.dp)
                )

            })

        navBarItems?.forEachIndexed{
                index, item ->
            NavigationBarItem(colors=NavigationBarItemDefaults.colors(
                selectedIconColor = DarkGrey,
                indicatorColor = LightBeige,
            ),

                selected = selectedIndex == index,
                onClick = { selectedIndex = index
                    item.route?.let {
                        navController.navigate(route = item.route)
                    }
                          },
                modifier = Modifier
                    .padding(vertical = 14.dp),
                label = {
                    item.title?.let {
                    Text(
                        text = it,
                        color = Beige,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                }, icon =   {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title,
                        tint = if (selectedIndex == index) Color.DarkGray else Beige,
                        modifier = Modifier.size(30.dp)

                    )
                })


        }
    }

}


