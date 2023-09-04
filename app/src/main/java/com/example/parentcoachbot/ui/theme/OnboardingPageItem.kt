package com.example.parentcoachbot.ui.theme

sealed class OnboardingPageItem(val headerText: String,
                              val descriptionText: String,
                              val pageIndex:Int){
    object ExploreTopics: OnboardingPageItem(
        headerText = "Explore \u2028Themes",
        descriptionText = "Learn more about different themes around Breastfeeding \uD83E\uDD31\uD83C\uDFFD and Sleep \uD83D\uDCA4",
        pageIndex = 0)
    object SearchQuestions: OnboardingPageItem(
        headerText = "Find \u2028Queries",
        descriptionText = "Search \uD83D\uDD0D from \u2028different queries the ones that best fit your doubts \uD83E\uDD14",
        pageIndex = 1)
    object SaveFavourites: OnboardingPageItem(
        headerText = "Save \u2028Favourites",
        descriptionText = "Save useful messages \u2028or images as favourites ⭐️ to look for them in \u2028the future.",
        pageIndex = 2)
}
