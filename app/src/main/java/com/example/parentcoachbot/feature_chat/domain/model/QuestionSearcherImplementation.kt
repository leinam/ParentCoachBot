package com.example.parentcoachbot.feature_chat.domain.model

import com.example.parentcoachbot.common.portugueseStopWordsCharArraySet
import com.example.parentcoachbot.common.zuluStopWordsCharArraySet
import com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases.QuestionUseCases
import com.example.parentcoachbot.feature_chat.domain.util.Language
import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.document.StoredField
import org.apache.lucene.document.TextField
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.ScoreDoc
import org.apache.lucene.search.TopDocs
import org.apache.lucene.search.similarities.BM25Similarity
import org.apache.lucene.store.Directory
import org.apache.lucene.store.RAMDirectory

class QuestionSearcherImplementation(
    val questionUseCases: QuestionUseCases,
) : QuestionSearcher {
    private val indexDir: Directory
    private var indexSearcher: IndexSearcher? = null
    private val questionTextFieldName = "questionText"
    private val questionIdFieldName = "questionId"

    private val analyzers: Map<String, Analyzer> =
        mapOf(
            Pair(Language.English.isoCode, StandardAnalyzer()),
            Pair(Language.Portuguese.isoCode, StandardAnalyzer(portugueseStopWordsCharArraySet)),
            Pair(Language.Zulu.isoCode, StandardAnalyzer(zuluStopWordsCharArraySet))
        )


    init {
        indexDir = RAMDirectory()
    }

    private fun createEnglishIndexWriter(currentLanguage: String): IndexWriter {
        val writerConfig = IndexWriterConfig(analyzers[currentLanguage])
        return IndexWriter(indexDir, writerConfig)
    }

    private fun createIndexSearcher(): IndexSearcher {
        val indexReader = DirectoryReader.open(indexDir)

        val searcher = IndexSearcher(indexReader)
        searcher.setSimilarity(BM25Similarity())
        return searcher
    }

    override fun search(queryText: String, currentLanguage: String): List<String> {
        // val query = FuzzyQuery(Term("content", queryText), 2)
        var searchResults = emptyList<String>()

        if (queryText.isNotEmpty()) {
            indexSearcher?.let {
                val queryParser = QueryParser(
                    questionTextFieldName,
                    analyzers[currentLanguage]
                )
                val query = queryParser.parse(queryText)
                val topDocs: TopDocs = it.search(query, 5)

                searchResults = topDocs.scoreDocs.mapNotNull { scoreDoc: ScoreDoc ->
                    val docId: Int = scoreDoc.doc
                    var questionId: String? = null
                    val document: Document? = it.doc(docId)

                    document?.let { doc ->
                        doc.get(questionTextFieldName)
                        questionId = doc.getField(questionIdFieldName).stringValue()
                    }

                    questionId
                }
            }
        }
        return searchResults
    }

    override fun populateIndex(
        questionsList: List<Question>,
        currentLanguage: String
    ) {
        val indexWriter: IndexWriter = createEnglishIndexWriter(currentLanguage = currentLanguage)

        questionsList.forEach { question ->

            question.questionText[currentLanguage]?.let {
                // println(it)
                val questionDocument = Document().apply {
                    add(
                        TextField(
                            questionTextFieldName,
                            it,
                            Field.Store.YES
                        )
                    )
                    add(
                        StoredField(
                            questionIdFieldName,
                            question._id
                        )
                    )
                }

                indexWriter.addDocument(questionDocument)

            }

        }

        indexWriter.commit()

        // println("writer has ${indexWriter.numDocs()}")
        if (indexWriter.numDocs() > 0) {
            indexSearcher = createIndexSearcher()
        }

        indexWriter.close()
    }


}