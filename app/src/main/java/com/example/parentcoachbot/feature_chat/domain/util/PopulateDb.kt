package com.example.parentcoachbot.feature_chat.domain.util

import com.example.parentcoachbot.R
import com.example.parentcoachbot.feature_chat.domain.model.Answer
import com.example.parentcoachbot.feature_chat.domain.model.AnswerThread
import com.example.parentcoachbot.feature_chat.domain.model.ChatSession
import com.example.parentcoachbot.feature_chat.domain.model.ChildProfile
import com.example.parentcoachbot.feature_chat.domain.model.ParentUser
import com.example.parentcoachbot.feature_chat.domain.model.Question
import com.example.parentcoachbot.feature_chat.domain.model.QuestionSession
import com.example.parentcoachbot.feature_chat.domain.model.Subtopic
import com.example.parentcoachbot.feature_chat.domain.model.Topic
import io.realm.kotlin.MutableRealm
import io.realm.kotlin.ext.realmListOf

class PopulateDb(private val realm: MutableRealm) {
    private val questionOneAnswerList = listOf(
        Answer().apply {
            this.answerText = "The only way to know if your baby is getting enough milk is by the number of wet diapers during the day."
            this.answerType = AnswerType.Info.code
        },

        Answer().apply {
            this.answerText = "After your milk has come in (usually about 3 days after birth), your baby should have at least 6 wet diapers per day, with clear urine."
            this.answerType = AnswerType.Info.code
        }
    )


    private val breastfeedingTopic: Topic = Topic().apply {
        this.title = "Breastfeeding"
        this.icon = R.drawable.breastfeeding_icon
    }

    private val defaultParentUser: ParentUser = ParentUser()
    private val childProfileTest: ChildProfile = ChildProfile().apply {
        this.parentUser = defaultParentUser._id
        this.name = "Joylyn"
    }

    private val childProfileTest2: ChildProfile = ChildProfile().apply {
        this.parentUser = defaultParentUser._id
        this.name = "Bob"
    }

    private val sampleChatSession: ChatSession = ChatSession().apply {
        this.chatTitle = "My First Chat"
        this.childProfile = childProfileTest._id
    }


    private val accesoriesBF = Subtopic().apply {
            this.title = "Accessories"
            this.topic = breastfeedingTopic._id
        }

    private val artificialMilkBF = Subtopic().apply {
            this.title = "Artificial Milks"
            this.topic = breastfeedingTopic._id
        }

    private val hygieneBF = Subtopic().apply {
            this.title = "Breast Hygiene"
            this.topic = breastfeedingTopic._id
                         }

    private val durationBF = Subtopic().apply {
            this.title = "Breastfeeding Duration"
            this.topic = breastfeedingTopic._id
        }

    private val painBF = Subtopic().apply {
            this.title = "Breastfeeding Pain"
            this.topic = breastfeedingTopic._id
        }

    private val problemsBF = Subtopic().apply {
            this.title = "Breastfeeding Problems"
            this.topic = breastfeedingTopic._id
        }

    private val techniqueBF = Subtopic().apply {
            this.title = "Breastfeeding Technique"
            this.topic = breastfeedingTopic._id
        }

    private val extractionBF = Subtopic().apply {
            this.title = "Breastfeeding Extraction and Preservation"
            this.topic = breastfeedingTopic._id
        }

    private val colicsBF = Subtopic().apply {
            this.title = "Colics and Cramps"
            this.topic = breastfeedingTopic._id
        }

    private val enoughMilkBF = Subtopic().apply {
            this.title = "Enough Milk"
            this.topic = breastfeedingTopic._id
    }

    private val breastfeedingSubtopicsList: List<Subtopic> = listOf(
        accesoriesBF, artificialMilkBF, hygieneBF, durationBF,
        painBF, problemsBF, techniqueBF,
        extractionBF, colicsBF, enoughMilkBF
    )


    operator fun invoke (){
        println("Attempting to pre-populate database")

        val answerThread: AnswerThread = AnswerThread().apply {
            title = "Enough milk"
            code = "A1"
            subtopic = accesoriesBF._id
        }
        realm.copyToRealm(answerThread)


        questionOneAnswerList.forEachIndexed { index, answer ->
            realm.copyToRealm(answer.apply
            {
                this.answerThread = answerThread.code
                this.answerThreadPosition = index
            })
        }

        val questionOne = Question().apply {
            this.questionTextEn = "Do I produce enough milk?"
            this.subtopics = realmListOf(accesoriesBF._id)
            this.answerThread = answerThread._id
        }

        println(questionOne.questionTextEn)

        val sampleQuestionSession: QuestionSession = QuestionSession().apply {
            this.question = questionOne._id
            this.chatSession = sampleChatSession._id
        }

        realm.copyToRealm(sampleChatSession)
        realm.copyToRealm(sampleQuestionSession)

        breastfeedingSubtopicsList.forEach {
            realm.copyToRealm(it)
        }

        realm.copyToRealm(defaultParentUser)
        realm.copyToRealm(childProfileTest)
        realm.copyToRealm(childProfileTest2)



        realm.copyToRealm(questionOne)
        realm.copyToRealm(breastfeedingTopic)
        println("Done pre-populating database")

    }



}