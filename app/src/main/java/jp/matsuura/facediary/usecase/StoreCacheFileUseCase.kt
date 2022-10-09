package jp.matsuura.facediary.usecase

import android.graphics.Bitmap
import jp.matsuura.facediary.data.datasource.CacheDir
import java.io.File
import javax.inject.Inject

class StoreCacheFileUseCase @Inject constructor(
    private val cacheDir: CacheDir,
) {

    suspend operator fun invoke(fileName: String, bitmap: Bitmap): File {
        return cacheDir.storeFaceImg(fileName = fileName, bitmap = bitmap)
    }
}