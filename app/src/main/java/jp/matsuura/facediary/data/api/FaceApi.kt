package jp.matsuura.facediary.data.api

import jp.matsuura.facediary.data.api.entity.FaceApiEntity
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FaceApi {

    @POST("/face/v1.0/detect?returnFaceId=true&returnFaceLandmarks=false&returnFaceAttributes=age,gender,headPose,smile,facialHair,glasses,emotion,hair,makeup,occlusion,accessories,blur,exposure,noise&recognitionModel=recognition_03&returnRecognitionModel=false&detectionModel=detection_01&faceIdTimeToLive=86400")
    suspend fun getFaceInfo(@Body requestBody: RequestBody): List<FaceApiEntity>

}