package jp.matsuura.facediary.data.repositories

import jp.matsuura.facediary.common.Response
import jp.matsuura.facediary.data.api.FaceApi
import jp.matsuura.facediary.data.model.FaceApiModel
import jp.matsuura.facediary.data.model.toModel
import jp.matsuura.facediary.enums.FaceApiError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FaceRepository @Inject constructor(
    private val faceApi: FaceApi,
){

    suspend fun getFaceInfo(file: File): Response<FaceApiModel, FaceApiError> {
        return withContext(Dispatchers.IO) {
            val requestBody = file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
            val res = faceApi.getFaceInfo(requestBody = requestBody).map { it.toModel() }
            if (res.isEmpty()) {
                Response.Error(error = FaceApiError.NO_ONE_EXISTED)
            } else if (res.size >= 2) {
                Response.Error(error = FaceApiError.MANY_PEOPLE_EXISTED)
            } else {
                Response.Success(value = res.first())
            }
        }
    }

}