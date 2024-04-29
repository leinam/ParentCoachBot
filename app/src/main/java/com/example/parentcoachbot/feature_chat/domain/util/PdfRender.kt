package com.example.parentcoachbot.feature_chat.domain.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.core.graphics.createBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


internal class PdfRender(
    val context: Context,
    val inputStream: InputStream,
    val fileName: String
) {

    val timestamp: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-kkmmss"))
    // val fileCopy = File.createTempFile(timestamp, null, context.cacheDir)
    private val fileCopy = File(context.cacheDir, fileName)



    init {
        if (!fileCopy.exists()) {
            // fileCopy.delete()

            val outputStream = FileOutputStream(fileCopy)
            copyToOutputStream(outputStream)
        }


    }

    // We get a page from the PDF doc by calling 'open'
    val fileDescriptor = ParcelFileDescriptor.open(
        fileCopy,
        ParcelFileDescriptor.MODE_READ_ONLY
    )

    private val pdfRenderer = PdfRenderer(fileDescriptor)
    val pageCount get() = pdfRenderer.pageCount
    private val mutex: Mutex = Mutex()
    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    val pageLists: List<Page> = List(pdfRenderer.pageCount) {
        Page(
            index = it,
            pdfRenderer = pdfRenderer,
            coroutineScope = coroutineScope,
            mutex = mutex
        )
    }

    fun copyToOutputStream(outputStream: OutputStream) {

        val buffer = ByteArray(8192)

        inputStream.use { input ->
            outputStream.use { fileOut ->

                while (true) {
                    val length = input.read(buffer)
                    if (length <= 0)
                        break
                    fileOut.write(buffer, 0, length)
                }
                fileOut.flush()
                fileOut.close()
            }
        }
        inputStream.close()
    }


    fun close() {
        pageLists.forEach {
            it.recycle()
        }
        pdfRenderer.close()
        fileDescriptor.close()
    }

    class Page(
        val mutex: Mutex,
        val index: Int,
        val pdfRenderer: PdfRenderer,
        val coroutineScope: CoroutineScope
    ) {
        var isLoaded = false

        var job: Job? = null

        val dimension = pdfRenderer.openPage(index).use { currentPage ->
            Dimension(
                width = currentPage.width,
                height = currentPage.height
            )
        }

        fun heightByWidth(width: Int): Int {
            val ratio = dimension.width.toFloat() / dimension.height
            return (ratio * width).toInt()
        }

        val pageContent = MutableStateFlow<Bitmap?>(null)

        fun load() {
            if (!isLoaded) {
                job = coroutineScope.launch {
                    mutex.withLock {
                        val newBitmap: Bitmap
                        pdfRenderer.openPage(index).use { currentPage ->
                            newBitmap = createBlankBitmap(
                                width = currentPage.width * 3,
                                height = currentPage.height * 3
                            )
                            currentPage.render(
                                newBitmap,
                                null,
                                null,
                                PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                            )
                        }
                        isLoaded = true
                        pageContent.emit(newBitmap)
                    }
                }
            }
        }

        fun recycle() {
            isLoaded = false
            val oldBitmap = pageContent.value
            pageContent.tryEmit(null)
            oldBitmap?.recycle()
        }

        private fun createBlankBitmap(
            width: Int,
            height: Int
        ): Bitmap {
            return createBitmap(
                width,
                height,
                Bitmap.Config.ARGB_8888
            ).apply {
                val canvas = Canvas(this)
                canvas.drawColor(android.graphics.Color.WHITE)
                canvas.drawBitmap(this, 0f, 0f, null)
            }
        }
    }

    data class Dimension(
        val width: Int,
        val height: Int
    )
}

