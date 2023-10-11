package com.example.parentcoachbot.feature_chat.domain.model

import com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases.QuestionUseCases
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
import org.mongodb.kbson.ObjectId

class QuestionSearcherImplementation(val questionUseCases: QuestionUseCases) : QuestionSearcher {
    private val indexDir: Directory
    private var indexSearcher: IndexSearcher? = null
    private val questionTextFieldName = "questionText"
    private val questionIdFieldName = "questionId"

    init {
        indexDir = RAMDirectory()
    }


    private fun createIndexWriter(): IndexWriter {
        val writerConfig = IndexWriterConfig(StandardAnalyzer())
        return IndexWriter(indexDir, writerConfig)
    }

    private fun createIndexSearcher(): IndexSearcher {
        val indexReader = DirectoryReader.open(indexDir)

        val searcher = IndexSearcher(indexReader)
        searcher.setSimilarity(BM25Similarity())
        return searcher
    }

    override fun search(queryText: String): List<ObjectId> {
        //val query = FuzzyQuery(Term("content", queryText), 2)
        var searchResults = emptyList<ObjectId>()

        if (queryText.isNotEmpty()) {
            indexSearcher?.let {
                val queryParser = QueryParser(questionTextFieldName, StandardAnalyzer())
                val query = queryParser.parse(queryText)
                val topDocs: TopDocs = it.search(query, 3)

                searchResults = topDocs.scoreDocs.mapNotNull { scoreDoc: ScoreDoc ->
                    val docId: Int = scoreDoc.doc
                    var questionId: ByteArray? = null
                    val document: Document? = it.doc(docId)

                    document?.let { doc ->
                        doc.get(questionTextFieldName)
                        questionId = doc.getField(questionIdFieldName).binaryValue().bytes
                    }

                    questionId?.let { id ->
                        ObjectId(id)
                    }
                }

            }
        }

        return searchResults
    }


    override fun populateIndex(questionsList: List<Question>) {
        val indexWriter: IndexWriter = createIndexWriter()

        println(questionsList)
        questionsList.forEach { question ->


            question.questionTextEn?.let {
                println(question.answerThread)

                val questionDocument = Document().apply {
                    add(
                        TextField(
                            questionTextFieldName,
                            question.questionTextEn,
                            Field.Store.YES
                        )
                    )
                    add(
                        StoredField(
                            questionIdFieldName,
                            question._id.toByteArray()
                        )
                    )
                }

                indexWriter.addDocument(questionDocument)

            }

        }

        indexWriter.commit()

        println("writer has ${indexWriter.numDocs()}")
        if (indexWriter.numDocs() > 0) {
            indexSearcher = createIndexSearcher()
        }

        indexWriter.close()
    }


}