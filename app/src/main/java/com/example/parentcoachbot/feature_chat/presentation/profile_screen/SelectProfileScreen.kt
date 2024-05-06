package com.example.parentcoachbot.feature_chat.presentation.profile_screen

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.example.parentcoachbot.MainActivity
import com.example.parentcoachbot.R
import com.example.parentcoachbot.feature_chat.domain.model.ChildProfile
import com.example.parentcoachbot.feature_chat.presentation.ConfirmDeleteDialog
import com.example.parentcoachbot.feature_chat.presentation.Screen
import com.example.parentcoachbot.ui.theme.BackgroundBeige
import com.example.parentcoachbot.ui.theme.BackgroundWhite
import com.example.parentcoachbot.ui.theme.Beige
import com.example.parentcoachbot.ui.theme.LightBeige
import com.example.parentcoachbot.ui.theme.LightGreen
import com.example.parentcoachbot.ui.theme.PlexSans
import com.example.parentcoachbot.ui.theme.PrimaryGreen

@Preview
@Composable
fun SelectProfileScreen(
    navController: NavController = rememberNavController(),
    profileState: State<ProfileStateWrapper> = mutableStateOf(ProfileStateWrapper()),
    onEvent: (ProfileEvent) -> Unit = {}
) {

    val context = LocalContext.current
    val profileStateWrapper = profileState.value
    val parentUser by profileStateWrapper.parentUserState.collectAsStateWithLifecycle()
    val childProfileList: List<ChildProfile> by profileStateWrapper.childProfilesListState.collectAsStateWithLifecycle()

    if (parentUser == null){
        // Toast.makeText(context, "Session Timed Out! Start Again Here", Toast.LENGTH_SHORT).show()

        val intent =
            Intent(context, MainActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = PrimaryGreen)
    ) {
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {


        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = PrimaryGreen.copy(alpha = 0.8f)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(36.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = stringResource(id = R.string.select_profile_label),
                    textAlign = TextAlign.Center,
                    fontFamily = PlexSans,
                    lineHeight = 42.sp,
                    fontWeight = FontWeight.Medium,
                    fontSize = 39.sp,
                    color = BackgroundWhite
                )

                Spacer(modifier = Modifier.size(20.dp))

                LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                    items(childProfileList) { childProfile ->

                        ProfileItem(
                            childProfile = childProfile,
                            navController = navController, onEvent = onEvent
                        )

                    }


                    items(1) {

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {

                            BoxWithConstraints(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(color = if (childProfileList.size < 3) LightGreen else Color.LightGray)
                                    .clickable {
                                        if (childProfileList.size < 3) {
                                            navController.navigate(Screen.AddProfileScreen.route)
                                        } else {
                                            Toast
                                                .makeText(
                                                    context,
                                                    "You cannot have more than three profiles at once. Long Press on a profile to delete.",
                                                    Toast.LENGTH_LONG
                                                )
                                                .show()
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_add_24),
                                    contentDescription = null, tint = Beige,
                                    modifier = Modifier.size(100.dp)
                                )


                            }

                            Text(
                                text = stringResource(id = R.string.new_profile_label),
                                color = LightBeige,
                                fontSize = 18.sp
                            )
                        }
                    }


                }

                Spacer(modifier = Modifier.size(50.dp))


            }
        }
    }


}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun ProfileItem(
    childProfile: ChildProfile = ChildProfile().apply { this.name = "Bob" },
    navController: NavController = rememberNavController(),
    onEvent: (ProfileEvent) -> Unit = {}
) {
    var isDeleteButtonVisible by remember {
        mutableStateOf(false)
    }

    var openAlertDialog = remember {
        mutableStateOf(false)
    }

    when {
        openAlertDialog.value -> {
            ConfirmDeleteDialog(
                onConfirmation = {

                    openAlertDialog.value = false
                    onEvent(ProfileEvent.DeleteProfile(childProfile))


                },
                onDismissRequest =
                {
                    openAlertDialog.value = false

                }, dialogText = "Are you sure you want to delete this profile?",
                dialogTitle = "Delete Profile"
            )
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box {
            BoxWithConstraints(
                modifier = Modifier
                    .padding(10.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color = Beige)
                    .combinedClickable(onClick = {
                        onEvent(ProfileEvent.SelectProfile(childProfile))
                        navController.navigate(Screen.ChatListScreen.route)
                    }, onLongClick = {
                        isDeleteButtonVisible = !isDeleteButtonVisible
                    })


            ) {
                Icon(
                    painter = painterResource(id = R.drawable.child_face),
                    contentDescription = null, tint = PrimaryGreen,
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.Center)
                )
            }

            androidx.compose.animation.AnimatedVisibility(visible = isDeleteButtonVisible) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(LightGreen)
                        .align(
                            Alignment.TopEnd
                        )
                        .clickable {
                            openAlertDialog.value = true
                            isDeleteButtonVisible = false
                        }
                ) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "delete-profile",
                        modifier = Modifier.padding(5.dp),
                        tint = BackgroundBeige
                    )
                }
            }


        }

        childProfile.name?.let {
            Text(
                text = it,
                color = LightBeige, fontSize = 18.sp
            )
        }
    }
}