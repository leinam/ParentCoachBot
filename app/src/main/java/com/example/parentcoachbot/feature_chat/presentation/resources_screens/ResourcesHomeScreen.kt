package com.example.parentcoachbot.feature_chat.presentation.resources_screens

import androidx.annotation.DrawableRes
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.parentcoachbot.R
import com.example.parentcoachbot.feature_chat.presentation.Screen
import com.example.parentcoachbot.feature_chat.presentation.chat_list.ChatListEvent
import com.example.parentcoachbot.feature_chat.presentation.chat_list.ChatListStateWrapper
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.CustomNavigationDrawer
import com.example.parentcoachbot.feature_chat.presentation.chat_screen.components.TopNavBar
import com.example.parentcoachbot.feature_chat.presentation.profile_screen.Country
import com.example.parentcoachbot.ui.theme.BackgroundBeige
import com.example.parentcoachbot.ui.theme.PlexSans
import com.example.parentcoachbot.ui.theme.PrimaryGreen
import com.example.parentcoachbot.ui.theme.TextGrey
import com.example.parentcoachbot.ui.theme.ThinGreen

sealed class ResourceItem(
    val title: Map<String, String>,
    val filename: String? = null,
    val contentType: String,
    @DrawableRes val imageIdList: List<Int>? = null,
    val country: String,
    val source: String
) {
    object RoadToHealth : ResourceItem(
        title = mapOf(
            "en" to "Road To Health Booklet",
            "pt" to "Caminho para a Saúde - Livreto"
        ),
        filename = "rthb_booklet.pdf",
        contentType = "PDF",
        country = Country.SouthAfrica.name,
        source = "SA Gov"
    )

    object DangerSignsPT : ResourceItem(
        title = mapOf(
            "en" to "Danger Signs",
            "pt" to "Quando levar o seu filho à Urgencia"
        ),
        contentType = "Image",
        imageIdList = listOf(R.drawable.dangersignpt),
        country = Country.Portugal.name,
        source = "Sociedade Portuguesa de Urgência e Emergência"
    )

    object DangerSignsSA : ResourceItem(
        title = mapOf(
            "en" to "Danger Signs",
            "pt" to "Quando levar o seu filho à Urgencia",
            "zu" to "Izimpawu Eziyingozi"
        ),
        contentType = "Image",
        imageIdList = listOf(R.drawable.rthb_danger_signs),
        country = Country.SouthAfrica.name,
        source = "Road To Health Booklet - SA Government"
    )

    object GoodNutritionSA : ResourceItem(
        title = mapOf(
            "en" to "Good Nutrition",
            "pt" to "Boa nutrição",
            "zu" to "Ukudla Okuhle"
        ),
        contentType = "Image",
        imageIdList = listOf(
            R.drawable.rthb_nutrition_1,
            R.drawable.rthb_nutrition_2,
            R.drawable.rthb_nutrition_3,
            R.drawable.rthb_nutrition_4
        ),
        country = Country.SouthAfrica.name,
        source = "Road To Health Booklet - SA Government"
    )

    object DevelopmentSA : ResourceItem(
        title = mapOf(
            "en" to "Child Development",
            "pt" to "Desenvolvimento Saudável",
            "zu" to "Ukuthuthukiswa Okunempilo"
        ),
        contentType = "Image",
        imageIdList = listOf(
            R.drawable.rthb_love1,
            R.drawable.rthb_development_milestones,
            R.drawable.rthb_development_2,
            R.drawable.rthb_developmemt3
        ),
        country = Country.SouthAfrica.name,
        source = "Road To Health Booklet - SA Government"
    )


    object PreventionSA : ResourceItem(
        title = mapOf(
            "en" to "Protecting Your Child",
            "pt" to "Protegendo seu filho",
            "zu" to "Ukuvikela Ingane Yakho"
        ),
        contentType = "Image",
        imageIdList = listOf(
            R.drawable.rthb_prevention,
            R.drawable.rthb_prevention_1,
        ),
        country = Country.SouthAfrica.name,
        source = "Road To Health Booklet - SA Government"
    )

    object SpecialCareSA : ResourceItem(
        title = mapOf(
            "en" to "Taking care of a sick or special needs child",
            "pt" to "Cuidar de uma criança doente ou com necessidades especiais",
            "zu" to "Ukunakekela ingane egulayo noma enezidingo ezikhethekile"
        ),
        contentType = "Image",
        imageIdList = listOf(
            R.drawable.rthb_special,
            R.drawable.rthb_special_2,
        ),
        country = Country.SouthAfrica.name,
        source = "Road To Health Booklet - SA Government"
    )

    object UsefulInfoSA : ResourceItem(
        title = mapOf(
            "en" to "Useful Info",
            "pt" to "Informação útil",
            "zu" to "Ulwazi Oluwusizo"
        ),
        contentType = "Image",
        imageIdList = listOf(
            R.drawable.rthb_info,
            R.drawable.rthb_info1,
        ),
        country = Country.SouthAfrica.name,
        source = "Road To Health Booklet - SA Government"
    )


}

@Preview
@Composable
fun ResourcesHomeScreen(
    navController: NavController = rememberNavController(),
    chatListViewModelState: State<ChatListStateWrapper> = mutableStateOf(ChatListStateWrapper()),
    onEvent: (ChatListEvent) -> Unit = {}

) {

    val chatListStateWrapper = chatListViewModelState.value
    val currentCountry by chatListStateWrapper.currentCountry.collectAsStateWithLifecycle()
    val currentLanguageCode = chatListStateWrapper.currentLanguageCode.collectAsStateWithLifecycle()

    val resourceList = listOf(
        ResourceItem.DangerSignsPT,
        ResourceItem.DangerSignsSA,
        ResourceItem.GoodNutritionSA,
        ResourceItem.DevelopmentSA,
        ResourceItem.PreventionSA,
        ResourceItem.SpecialCareSA,
        ResourceItem.UsefulInfoSA
    ).filter { resourceItem -> resourceItem.country == currentCountry }


    val scope = rememberCoroutineScope()
    val drawerSelectedItemIndex = rememberSaveable { mutableIntStateOf(6) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)



    Scaffold(
        topBar = {
            TopNavBar(
                navController = navController,
                screenIndex = 6,
                drawerState = drawerState,
                scope = scope
            )
        },

        )
    { contentPadding ->
        CustomNavigationDrawer(
            drawerState = drawerState,
            drawerSelectedItemIndex = drawerSelectedItemIndex,
            navController = navController,
            contentPadding = contentPadding,
            content = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = BackgroundBeige)
                        .padding(contentPadding)

                ) {

                    Column {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(PrimaryGreen.copy(alpha = 0.3f))
                                .padding(17.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = stringResource(id = R.string.resources_label),
                                color = PrimaryGreen,
                                textAlign = TextAlign.Center,
                                fontFamily = PlexSans,
                                fontWeight = FontWeight.Normal,
                                fontSize = 20.sp
                            )


                        }



                        LazyColumn {
                            items(resourceList) { resource ->
                                Box(modifier = Modifier
                                    .padding(10.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(color = ThinGreen)
                                    .fillMaxWidth()
                                    .height(110.dp)
                                    .padding(10.dp)
                                    .clickable {

                                        if (resource.contentType == "PDF") {
                                            // onEvent(ChatListEvent.SelectPDFResource(resourceItem = resource))
                                            // navController.navigate(Screen.PDFResourceScreen.route)
                                        } else if (resource.contentType == "Image") {

                                            onEvent(
                                                ChatListEvent.SelectImageResource(
                                                    resourceItem = resource
                                                )
                                            )

                                            navController.navigate(Screen.ImageResourceScreen.route)

                                        }


                                    }
                                )
                                {

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .align(Alignment.CenterStart),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        Column(horizontalAlignment = Alignment.Start) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.padding(bottom = 3.dp)
                                            ) {

                                                (resource.title[currentLanguageCode.value]
                                                    ?: resource.title["en"])?.let {
                                                    Text(
                                                        text = it,
                                                        fontSize = 18.sp,
                                                        fontFamily = PlexSans,
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = TextGrey
                                                    )
                                                }
                                            }



                                            Row(verticalAlignment = Alignment.CenterVertically) {

                                                Text(
                                                    buildAnnotatedString {
                                                        withStyle(
                                                            style = SpanStyle(
                                                                fontFamily = PlexSans,
                                                                color = TextGrey,
                                                                fontWeight = FontWeight.SemiBold,
                                                                fontSize = 12.sp
                                                            )
                                                        ) {
                                                            append(text = stringResource(id = R.string.source_label) + " ")
                                                        }

                                                        withStyle(
                                                            style = SpanStyle(
                                                                fontFamily = PlexSans,
                                                                color = TextGrey,
                                                                fontWeight = FontWeight.Normal,
                                                                fontSize = 12.sp
                                                            )
                                                        ) {
                                                            append(text = resource.source)
                                                        }
                                                    }

                                                )

                                            }


                                        }



                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                                            contentDescription = null, tint = TextGrey
                                        )
                                    }


                                }
                            }
                        }


                    }


                }
            })

    }
}
