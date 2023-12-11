package com.example.parentcoachbot.feature_chat.presentation.chat_screen.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.parentcoachbot.ui.theme.Beige
import com.example.parentcoachbot.ui.theme.ChatListGreen
import com.example.parentcoachbot.ui.theme.PrimaryGreen
import com.example.parentcoachbot.ui.theme.drawerItemsList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
@Preview
fun CustomNavigationDrawer(
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Open),
    drawerSelectedItemIndex: MutableState<Int> = mutableIntStateOf(0),
    content: @Composable () -> Unit = { },
    scope: CoroutineScope = rememberCoroutineScope(),
    navController: NavController = rememberNavController(),
    currentChildProfileName: String = "",
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = Beige,
                modifier =
                Modifier.width(300.dp)
                    .padding(contentPadding))
            {
                drawerItemsList.forEachIndexed { index, navBarItem ->
                    NavigationDrawerItem(
                        label = {
                            if (index == 0) navBarItem.title?.let {
                                Text(
                                    text = stringResource(
                                        id = it
                                    ) + ": $currentChildProfileName "
                                )
                            } else navBarItem.title?.let { Text(text = stringResource(id = it)) }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = ChatListGreen,
                            unselectedContainerColor = Beige,
                            unselectedTextColor = PrimaryGreen,
                            selectedTextColor = PrimaryGreen
                        ),
                        selected = index == drawerSelectedItemIndex.value,
                        onClick = {
                            drawerSelectedItemIndex.value = index
                            scope.launch {
                                navBarItem.route?.let { navController.navigate(route = it) }
                                drawerState.close()
                            }
                        },
                        icon = {
                            Icon(painter = painterResource(id = navBarItem.icon),
                                contentDescription = navBarItem.title?.let { stringResource(it) }
                            )
                        },
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }
    ) {
        content()
    }
}