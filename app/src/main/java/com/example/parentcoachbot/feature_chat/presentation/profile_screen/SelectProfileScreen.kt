package com.example.parentcoachbot.feature_chat.presentation.profile_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.parentcoachbot.R
import com.example.parentcoachbot.feature_chat.domain.model.ChildProfile
import com.example.parentcoachbot.feature_chat.presentation.Screen
import com.example.parentcoachbot.ui.theme.BackgroundWhite
import com.example.parentcoachbot.ui.theme.Beige
import com.example.parentcoachbot.ui.theme.LightBeige
import com.example.parentcoachbot.ui.theme.LightGreen
import com.example.parentcoachbot.ui.theme.PlexSans
import com.example.parentcoachbot.ui.theme.PrimaryGreen

@Preview
@Composable
fun SelectProfileScreen(navController: NavController = rememberNavController(),
                        profileState: State<ProfileStateWrapper> = mutableStateOf(ProfileStateWrapper()),
                        onEvent: (ProfileEvent) -> Unit = {}
) {

    val profileStateWrapper = profileState.value
    val childProfileList: List<ChildProfile> by profileStateWrapper.childProfilesListState.collectAsStateWithLifecycle()


    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = PrimaryGreen.copy(alpha = 0.8f)),
    contentAlignment = Alignment.Center
    ){

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
            ){

            Text(text = stringResource(id = R.string.select_profile_label),
                textAlign = TextAlign.Center,
                fontFamily = PlexSans,
                fontWeight = FontWeight.Medium,
                fontSize = 30.sp,
                color = BackgroundWhite
            )

            Spacer(modifier = Modifier.size(20.dp))

            LazyVerticalGrid(columns = GridCells.Fixed(2)){
                items(childProfileList){
                        childProfile ->

                    ProfileItem(childProfile = childProfile,
                    navController = navController, onEvent = onEvent)

                }

                items(1){

                    Column(horizontalAlignment = Alignment.CenterHorizontally){

                        BoxWithConstraints(
                            modifier = Modifier
                                .padding(10.dp)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(5.dp))
                                .background(color = Beige)
                                .clickable {
                                    navController.navigate(Screen.AddProfileScreen.route)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_add_24),
                                contentDescription = null, tint = LightGreen,
                                modifier = Modifier.size(90.dp)
                            )
                        }

                        Text(text = stringResource(id = R.string.new_profile_label), color = LightBeige, fontSize = 18.sp)
                    }
                }

            }
        }
    }


}

@Preview
@Composable
fun ProfileItem(childProfile: ChildProfile =  ChildProfile().apply { this.name = "Bob" },
                navController: NavController = rememberNavController(),
                onEvent: (ProfileEvent) -> Unit = {}) {
    Column(horizontalAlignment = Alignment.CenterHorizontally){

        BoxWithConstraints(
            modifier = Modifier
                .padding(10.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(5.dp))
                .background(color = Beige)
                .clickable {
                    onEvent(ProfileEvent.selectProfile(childProfile))
                    navController.navigate(Screen.ChatListScreen.route)
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.breastfeeding_icon),
                contentDescription = null, tint = LightGreen,
                modifier = Modifier.size(100.dp)
            )
        }

        childProfile.name?.let { Text(text = it,
            color = LightBeige, fontSize = 18.sp) }
    }
}