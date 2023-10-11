package com.example.parentcoachbot.feature_chat.domain.util

import android.content.Context
import com.example.parentcoachbot.feature_chat.domain.model.Answer
import com.example.parentcoachbot.feature_chat.domain.model.AnswerThread
import com.example.parentcoachbot.feature_chat.domain.model.Question
import com.example.parentcoachbot.feature_chat.domain.model.Subtopic
import io.realm.kotlin.MutableRealm
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.mongodb.kbson.ObjectId

class ContentImporter(
    private val context: Context,
    private val realm: MutableRealm,
    private val topicId: ObjectId
) {
    fun importContent() {
        val start = System.currentTimeMillis()
        val workbook = importWorkbook()

        workbook?.let {
            importSubtopics(workbook)
            importQuestions(workbook)
            importAnswerThreads(workbook)

            workbook.close()
        }

        println((System.currentTimeMillis() - start))
    }

    private fun importWorkbook(): XSSFWorkbook? {
        val assetManager = context.assets

        return try {
            val inputStream = assetManager.open("ContentTest.xlsx")
            XSSFWorkbook(inputStream)

        } catch (e: Exception) {
            println(e.message)
            null
        }

    }

    private fun importQuestions(workbook: XSSFWorkbook) {
        val questionsSheet = workbook.getSheet("questions")

        for (row in questionsSheet) {
            val questionTextEn = row.getCell(0)?.toString()?.trim()
            val questionAnswerThread = row.getCell(5)?.toString()?.trim()
            val questionSubtopic = row.getCell(6)?.toString()?.trim()

            questionTextEn?.let {
                if (it.isNotBlank()) {
                    realm.copyToRealm(Question().apply {
                        this.questionTextEn = questionTextEn
                        this.answerThread = questionAnswerThread
                        this.subtopic = questionSubtopic
                    })

                    println("$questionTextEn")
                }

            }


        }
    }

    private fun importSubtopics(workbook: XSSFWorkbook) {
        val subtopicsSheet = workbook.getSheet("subtopics")

        for (row in subtopicsSheet) {
            val subtopicTitle = row.getCell(0)?.toString()?.trim()
            val subtopicCode = row.getCell(1)?.toString()?.trim()
            val subtopicDescription = row.getCell(5)?.toString()?.trim()

            realm.copyToRealm(
                Subtopic().apply {
                    this.title = subtopicTitle
                    this.code = subtopicCode
                    this.description = subtopicDescription
                    this.topic = topicId
                })
        }

    }

    private fun importAnswerThreads(workbook: XSSFWorkbook) {
        val answerThreadSheet = workbook.getSheet("answers")

        for (row in answerThreadSheet) {

            val answerThreadCode = row.getCell(0)?.toString()?.trim()
            val answerThreadTitle = row.getCell(2)?.toString()?.trim()
            val answerThreadSubtopic = row.getCell(1)?.toString()?.trim()

            println("$answerThreadCode: $answerThreadTitle: $answerThreadSubtopic")

            answerThreadCode?.let {
                if (answerThreadCode.isNotBlank()) {
                    val answerThread = AnswerThread().apply {
                        this.title = answerThreadTitle
                        this.code = answerThreadCode
                        this.subtopic = answerThreadSubtopic
                    }

                    realm.copyToRealm(answerThread)

                    val answerTextsEn: List<String?> = listOf(
                        row.getCell(3)?.toString()?.trim(),
                        row.getCell(8)?.toString()?.trim(),
                        row.getCell(13)?.toString()?.trim(),
                        row.getCell(18)?.toString()?.trim(),
                        row.getCell(23)?.toString()?.trim(),
                        row.getCell(28)?.toString()?.trim(),
                        row.getCell(33)?.toString()?.trim(),
                        row.getCell(42)?.toString()?.trim(),
                        row.getCell(47)?.toString()?.trim(),
                        row.getCell(52)?.toString()?.trim(),
                        row.getCell(57)?.toString()?.trim()
                    )

                    answerTextsEn.forEachIndexed() { index, answerTextEn ->
                        answerTextEn?.let {
                            if (it.isNotBlank()) {
                                println("Part $index: $answerTextEn")

                                realm.copyToRealm(Answer().apply {
                                    this.answerThread = answerThread.code
                                    this.answerThreadPosition = index
                                    this.answerTextEn = it
                                })
                            }
                        }

                    }
                }
            }
        }
    }


}