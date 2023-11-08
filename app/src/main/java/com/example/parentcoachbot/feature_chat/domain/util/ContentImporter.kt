package com.example.parentcoachbot.feature_chat.domain.util

import android.content.Context
import com.example.parentcoachbot.feature_chat.domain.model.Answer
import com.example.parentcoachbot.feature_chat.domain.model.AnswerThread
import com.example.parentcoachbot.feature_chat.domain.model.Question
import com.example.parentcoachbot.feature_chat.domain.model.Subtopic
import io.realm.kotlin.MutableRealm
import io.realm.kotlin.ext.realmDictionaryOf
import io.realm.kotlin.types.RealmDictionary
import org.apache.poi.xssf.usermodel.XSSFWorkbook

class ContentImporter(
    private val context: Context,
    private val realm: MutableRealm,
    private val topicId: String,
    private val userId: String
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
            val questionTextEn = row.getCell(1)?.stringCellValue?.trim()
            val questionTextPt = row.getCell(4)?.stringCellValue?.trim()
            val questionTextZu = row.getCell(5)?.stringCellValue?.trim()
            val questionAnswerThread = row.getCell(2)?.stringCellValue?.trim()
            val questionSubtopic = row.getCell(3)?.stringCellValue?.trim()
            // println("$questionTextEn: $questionTextPt : $questionTextZu")

            questionTextEn?.let {
                val questionTextDict = realmDictionaryOf(
                    Pair(Language.English.isoCode, questionTextEn ?: ""),
                    Pair(Language.Portuguese.isoCode, questionTextPt ?: ""),
                    Pair(Language.Zulu.isoCode, questionTextZu?: "")
                )

                if (it.isNotBlank()) {
                    realm.copyToRealm(Question().apply {
                        this.questionText = questionTextDict
                        this.answerThread = questionAnswerThread
                        this.subtopic = questionSubtopic
                        this._partition = userId
                    })

                }

            }


        }
    }

    private fun importSubtopics(workbook: XSSFWorkbook) {
        val subtopicsSheet = workbook.getSheet("subtopics")

        for (row in subtopicsSheet) {


            val subtopicCode = row.getCell(0)?.stringCellValue?.trim()
            val subtopicTitleEn = row.getCell(1)?.stringCellValue?.trim()
            val subtopicTitlePt = row.getCell(2)?.stringCellValue?.trim()
            val subtopicTitleZu = row.getCell(3)?.stringCellValue?.trim()
            val subtopicDescriptionEn = row.getCell(4)?.stringCellValue?.trim()
            val subtopicDescriptionPt = row.getCell(5)?.stringCellValue?.trim()
            val subtopicDescriptionZu = row.getCell(6)?.stringCellValue?.trim()

            subtopicCode?.let {
                // println("$subtopicCode : $subtopicTitleEn: $subtopicTitlePt: $subtopicTitleZu")
                val subtopicTitleDict = realmDictionaryOf(
                    Pair(Language.English.isoCode, subtopicTitleEn ?: ""),
                    Pair(Language.Portuguese.isoCode, subtopicTitlePt ?: ""),
                    Pair(Language.Zulu.isoCode, subtopicTitleZu  ?: "")
                )

                val subtopicDescriptionDict = realmDictionaryOf(
                    Pair(Language.English.isoCode, subtopicDescriptionEn  ?: ""),
                    Pair(Language.Portuguese.isoCode, subtopicDescriptionPt  ?: ""),
                    Pair(Language.Zulu.isoCode, subtopicDescriptionZu  ?: "")
                )

                realm.copyToRealm(
                    Subtopic().apply {
                        this.title = subtopicTitleDict
                        this.code = subtopicCode
                        this.description = subtopicDescriptionDict
                        this.topic = topicId
                        this._partition = userId
                    })
            }


        }

    }

    private fun importAnswerThreads(workbook: XSSFWorkbook) {
        val answerThreadSheet = workbook.getSheet("answers")

        for (row in answerThreadSheet) {

            val answerThreadCode = row.getCell(0)?.stringCellValue?.trim()
            val answerThreadTitle = row.getCell(2)?.stringCellValue?.trim()
            val answerThreadSubtopic = row.getCell(1)?.stringCellValue?.trim()

            // println("$answerThreadCode: $answerThreadTitle: $answerThreadSubtopic")

            answerThreadCode?.let {
                if (answerThreadCode.isNotBlank()) {
                    val answerThread = AnswerThread().apply {
                        this.title = answerThreadTitle
                        this.code = answerThreadCode
                        this.subtopic = answerThreadSubtopic
                        this._partition = userId
                    }

                    realm.copyToRealm(answerThread)

                    val answerTextsEn: List<String?> = listOf(
                        row.getCell(3)?.stringCellValue?.trim(),
                        row.getCell(8)?.stringCellValue?.trim(),
                        row.getCell(13)?.stringCellValue?.trim(),
                        row.getCell(18)?.stringCellValue?.trim(),
                        row.getCell(23)?.stringCellValue?.trim(),
                        row.getCell(28)?.stringCellValue?.trim(),
                        row.getCell(33)?.stringCellValue?.trim(),
                        row.getCell(42)?.stringCellValue?.trim(),
                        row.getCell(47)?.stringCellValue?.trim(),
                        row.getCell(52)?.stringCellValue?.trim(),
                        row.getCell(57)?.stringCellValue?.trim()
                    )

                    val answerTextsPt: List<String?> = listOf(
                        row.getCell(5)?.stringCellValue?.trim(),
                        row.getCell(10)?.stringCellValue?.trim(),
                        row.getCell(15)?.stringCellValue?.trim(),
                        row.getCell(20)?.stringCellValue?.trim(),
                        row.getCell(25)?.stringCellValue?.trim(),
                        row.getCell(30)?.stringCellValue?.trim(),
                        row.getCell(35)?.stringCellValue?.trim(),
                        row.getCell(44)?.stringCellValue?.trim(),
                        row.getCell(49)?.stringCellValue?.trim(),
                        row.getCell(54)?.stringCellValue?.trim(),
                        row.getCell(59)?.stringCellValue?.trim()
                    )

                    val answerTextsZu: List<String?> = listOf(
                        row.getCell(6)?.stringCellValue?.trim(),
                        row.getCell(11)?.stringCellValue?.trim(),
                        row.getCell(16)?.stringCellValue?.trim(),
                        row.getCell(21)?.stringCellValue?.trim(),
                        row.getCell(26)?.stringCellValue?.trim(),
                        row.getCell(31)?.stringCellValue?.trim(),
                        row.getCell(36)?.stringCellValue?.trim(),
                        row.getCell(45)?.stringCellValue?.trim(),
                        row.getCell(50)?.stringCellValue?.trim(),
                        row.getCell(55)?.stringCellValue?.trim(),
                        row.getCell(60)?.stringCellValue?.trim()
                    )



                    answerTextsEn.forEachIndexed() { index, answerTextEn ->
                        answerTextEn?.let {
                            if (it.isNotBlank()) {
                                println("Part $index: $answerTextEn: ${answerTextsPt[index]}: ${answerTextsZu[index]}")

                                val answerTextDict: RealmDictionary<String> = realmDictionaryOf(
                                    Pair(Language.English.isoCode, answerTextEn ?: ""),
                                    Pair(Language.Portuguese.isoCode, answerTextsPt[index] ?: ""),
                                    Pair(Language.Zulu.isoCode, answerTextsZu[index] ?: "")
                                )

                                realm.copyToRealm(Answer().apply {
                                    this.answerThread = answerThread.code
                                    this.answerThreadPosition = index
                                    this.answerText = answerTextDict
                                    this._partition = userId
                                })
                            }
                        }

                    }
                }
            }
        }
    }


}