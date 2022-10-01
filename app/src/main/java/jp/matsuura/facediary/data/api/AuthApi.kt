package jp.matsuura.facediary.data.api

import jp.matsuura.facediary.data.api.entity.ApiEntity
import jp.matsuura.facediary.data.api.entity.AuthEntity
import jp.matsuura.facediary.data.api.request.ChangePasswordRequest
import jp.matsuura.facediary.data.api.request.SignUpRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {

    @GET("/v1/auth/login")
    suspend fun login(@Query("email") email: String, @Query("password") password: String): Response<AuthEntity>

    @POST("/v1/auth/create_user")
    suspend fun createUser(@Body body: SignUpRequest): Response<ApiEntity>

    @GET("/v1/auth/verify_email_token")
    suspend fun verifyToken(@Query("token") token: String): Response<AuthEntity>

    @GET("/v1/auth/reset_password")
    suspend fun resetPassword(@Query("email") email: String): Response<ApiEntity>

    @POST("/v1/auth/change_password")
    suspend fun changePassword(@Body body: ChangePasswordRequest): Response<ApiEntity>

}