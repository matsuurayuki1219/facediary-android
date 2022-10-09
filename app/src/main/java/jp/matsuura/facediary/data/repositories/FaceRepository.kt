package jp.matsuura.facediary.data.repositories

import jp.matsuura.facediary.data.api.FaceApi
import jp.matsuura.facediary.data.model.FaceApiModel
import jp.matsuura.facediary.data.model.toModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FaceRepository @Inject constructor(
    private val faceApi: FaceApi,
){

    suspend fun getFaceInfo(file: File): List<FaceApiModel> {
        return withContext(Dispatchers.IO) {
            val requestBody = file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
            faceApi.getFaceInfo(file = requestBody).map { it.toModel() }
        }
    }
}