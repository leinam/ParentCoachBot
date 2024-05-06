package com.example.parentcoachbot.feature_chat.presentation.contact_screen

import androidx.annotation.StringRes
import com.example.parentcoachbot.R
import com.example.parentcoachbot.feature_chat.presentation.profile_screen.Country

sealed class ContactScreenEvent {
    data class ViewContactCategory(val contactCategory: ContactCategory) : ContactScreenEvent()
    data class SelectContact(val contactItem: ContactItem) : ContactScreenEvent()

}

sealed class ContactCategory(@StringRes val title: Int, val id: String) {
    object MentalHealth :
        ContactCategory(title = R.string.mental_health_header, id = "mental_health")

    object Emergency : ContactCategory(title = R.string.medical_emergency_label, id = "emergency")

    object ResearchTeam : ContactCategory(title = R.string.team_contact_label, id = "research_team")
}

sealed class ContactItem(
    @StringRes val title: Int,
    @StringRes val description: Int,
    val categoryId: String,
    val id: String,
    val country: String,
    val phoneContact: String?,
    val emailContact: String? = null
) {
    object SAPolice : ContactItem(
        country = Country.SouthAfrica.name,
        id = "SA Police",
        categoryId = ContactCategory.Emergency.id,
        description = R.string.sa_police_title,
        title = R.string.sa_police_title,
        phoneContact = "10111"
    )

    object SAAmbulance : ContactItem(
        country = Country.SouthAfrica.name,
        id = "SA Ambulance",
        categoryId = ContactCategory.Emergency.id,
        description = R.string.sa_emergency_contact_description,
        title = R.string.sa_emergency_contact,
        phoneContact = "10177"
    )

    object SADAG : ContactItem(
        country = Country.SouthAfrica.name,
        id = "SADAG",
        categoryId = ContactCategory.MentalHealth.id,
        description = R.string.sadag_description,
        title = R.string.sadag_contact_title,
        phoneContact = "0800567567"
    )

    object Adcock : ContactItem(
        country = Country.SouthAfrica.name,
        id = "Adcock",
        categoryId = ContactCategory.MentalHealth.id,
        description = R.string.adcock_contact_description,
        title = R.string.adcock_contact_title,
        phoneContact = "0800708090"
    )

    object SaCrisisHotline : ContactItem(
        country = Country.SouthAfrica.name,
        id = "SA Crisis Hotline",
        categoryId = ContactCategory.MentalHealth.id,
        description = R.string.sa_crisis_hotline_description,
        title = R.string.crisis_hotline_title,
        phoneContact = "0800212223"
    )

    object PTMedicalEmergency : ContactItem(
        country = Country.Portugal.name,
        id = "Emergencia Medica",
        categoryId = ContactCategory.Emergency.id,
        description = R.string.pt_medical_emergency_description,
        title = R.string.pt_medical_emergency,
        phoneContact = "112"
    )

    object PTMedicalEmergency2 : ContactItem(
        country = Country.Portugal.name,
        id = "Linha Saude 24",
        categoryId = ContactCategory.Emergency.id,
        description = R.string.pt_medical_emergency_2_description,
        title = R.string.pt_medical_emergency_2,
        phoneContact = "808242424"
    )

    object PTMentalHealth : ContactItem(
        country = Country.Portugal.name,
        id = "Serviço de Aconselhamento Psicológico",
        categoryId = ContactCategory.MentalHealth.id,
        description = R.string.pt_mental_health_contact_description,
        title = R.string.pt_mental_health_contact,
        phoneContact = "808242424"
    )

    object PTMentalHealth2 : ContactItem(
        country = Country.Portugal.name,
        id = "SOS VOZ AMIGA",
        categoryId = ContactCategory.MentalHealth.id,
        description = R.string.pt_medical_emergency_2_description,
        title = R.string.pt_mental_health_contact_2,
        phoneContact = "213544545"
    )

    object PTResearchContact : ContactItem(
        country = Country.Portugal.name,
        id ="PT Research Contact",
        categoryId = ContactCategory.ResearchTeam.id,
        description = R.string.research_contact_description,
        title = R.string.pt_team_contact,
        phoneContact = null
    )

    object SAResearchContact : ContactItem(
        country = Country.Portugal.name,
        id ="SA Research Contact",
        categoryId = ContactCategory.ResearchTeam.id,
        description = R.string.research_contact_description,
        title = R.string.sa_team_contact,
        phoneContact = null
    )


}