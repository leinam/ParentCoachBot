import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.parentcoachbot.R
import com.example.parentcoachbot.feature_chat.presentation.Screen
import com.example.parentcoachbot.ui.theme.NavBarItem
import kotlinx.coroutines.launch


@Composable
fun NavigationDrawerScreen(content: @Composable() ()->Unit,
                           navController: NavController = rememberNavController()) {
    val scope = rememberCoroutineScope()
    val navBarItems = listOf(
        NavBarItem("New Chat",
            R.drawable.newchat_icon,
            route = Screen.ChatScreen.route),

        NavBarItem("Help",
            R.drawable.help_icon,
            route = "help"),

        NavBarItem("Chats",
            R.drawable.chats_icon,
            route = Screen.ChatListScreen.route),

        NavBarItem("Saved",
            R.drawable.favourites_icon,
            route = "favourites")
    )

    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet()
            {
                navBarItems.forEachIndexed { index, navBarItem ->
                    NavigationDrawerItem(
                        label = { navBarItem.title?.let { Text(text = it) } },
                        selected = index == selectedIndex,
                        onClick = {
                            selectedIndex = index
                            scope.launch{
                                drawerState.close()
                                navController.navigate(route = navBarItem.route)
                            }

                        },
                        icon = {
                            Icon(painter = painterResource(id = navBarItem.icon),
                                contentDescription = navBarItem.title)
                        },
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }
    ){
        content()
    }
}