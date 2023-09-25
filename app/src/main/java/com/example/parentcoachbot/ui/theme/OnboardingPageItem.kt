package com.example.parentcoachbot.ui.theme

import androidx.annotation.StringRes
import com.example.parentcoachbot.R

sealed class OnboardingPageItem(@StringRes val headerText: Int,
                                @StringRes val descriptionText: Int,
                                val pageIndex:Int){
    object ExploreTopics: OnboardingPageItem(
        headerText = R.string.explore_topics_header_text,
        descriptionText = R.string.explore_topics_description_text,
        pageIndex = 0)
    object SearchQuestions: OnboardingPageItem(
        headerText = R.string.search_questions_header_text,
        descriptionText = R.string.search_questions_description_text,
        pageIndex = 1)
    object SaveFavourites: OnboardingPageItem(
        headerText = R.string.save_favourites_header_text,
        descriptionText = R.string.save_favourites_description_text,
        pageIndex = 2)
}
