
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.parentcoachbot.R
import com.example.parentcoachbot.feature_chat.domain.model.Topic
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.TopNavBar
import com.example.parentcoachbot.ui.theme.BackgroundBeige
import com.example.parentcoachbot.ui.theme.PlexSans
import com.example.parentcoachbot.ui.theme.PrimaryGreen
import com.example.parentcoachbot.ui.theme.TextGrey
import com.example.parentcoachbot.ui.theme.ThinGreen
import com.example.parentcoachbot.ui.theme.drawerItemsList
import kotlinx.coroutines.launch

@Preview
@Composable
fun ResourcesHomeScreen(navController: NavController = rememberNavController()) {

    val topicsList = listOf<Topic>(Topic().apply {
        this.titleEn = "Breastfeeding"
        this.icon = R.drawable.breastfeeding_icon
    })
    val scope = rememberCoroutineScope()
    var drawerSelectedItemIndex by rememberSaveable { mutableIntStateOf(4) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet()
            {
                drawerItemsList.forEachIndexed { index, navBarItem ->
                    NavigationDrawerItem(
                        label = { navBarItem.title?.let { Text(text = stringResource(id = it)) } },
                        selected = index == drawerSelectedItemIndex,
                        onClick = {
                            drawerSelectedItemIndex = index
                            scope.launch{
                                drawerState.close()
                                navBarItem.route?.let { navController.navigate(route = it) }
                            }
                        },
                        icon = { Icon(painter = painterResource(id = navBarItem.icon),
                            contentDescription = navBarItem.title?.let { stringResource(it) }
                        ) },
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }
    ){
        Scaffold(
            topBar = {
                TopNavBar(
                    navController=navController,
                    screenIndex = 6,
                    drawerState = drawerState,
                    scope = scope)
            },

            )
        {contentPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = BackgroundBeige)
                    .padding(contentPadding)

            ){

                Column {

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .background(PrimaryGreen.copy(alpha = 0.3f))
                        .padding(17.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically){

                        Text(text = "Resources",
                            color = PrimaryGreen,
                            textAlign = TextAlign.Center,
                            fontFamily = PlexSans,
                            fontWeight = FontWeight.Normal,
                            fontSize = 20.sp)


                    }

                    LazyColumn {
                        items(topicsList){
                                topic ->
                            Box(modifier = Modifier
                                .padding(10.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(color = ThinGreen)
                                .fillMaxWidth()
                                .height(70.dp)
                                .padding(10.dp)
                                .clickable {

                                }
                            )
                            {

                                Row(modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {


                                    Row{
                                        topic.icon?.let {
                                            Icon(painter = painterResource(id = it),
                                                contentDescription = null, tint = TextGrey
                                                , modifier = Modifier.padding(end = 12.dp))
                                        }

                                        Text(
                                            text = topic.titleEn ?: "",
                                            fontSize = 18.sp,
                                            fontFamily = PlexSans,
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextGrey
                                        )
                                    }

                                    Icon(painter = painterResource(id = R.drawable.baseline_keyboard_arrow_down_30),
                                        contentDescription = null, tint = TextGrey
                                    )
                                }



                            }
                        }
                    }


                }

            }
        }
    }
}