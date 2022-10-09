package jp.matsuura.facediary.data.datasource

import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Named

class CacheDir @Inject constructor(
    @Named("CACHE_DIR") private val cacheDir: File,
) {

    fun storeFaceImg(fileName: String, bitmap: Bitmap): File {
        val dir = File(cacheDir, DIR_PATH)
        if (!dir.exists()) {
            dir.mkdir()
        }
        val file = File(dir, fileName)
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.close()
        return file
    }

    fun deleteFaceImg(file: File) {
        file.delete()
    }

    companion object {
        private const val DIR_PATH = "image"
    }
}