package jp.matsuura.facediary.api

import jp.matsuura.facediary.api.entity.ApiResponse
import jp.matsuura.facediary.api.entity.AuthResponse
import jp.matsuura.facediary.api.request.LoginRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {

    @GET("/v1/auth/login")
    suspend fun login(@Query("email") email: String, @Query("password") password: String): AuthResponse

    @POST("/v1/auth/create_user")
    suspend fun createUser(@Body body: LoginRequest): ApiResponse

    @POST
    suspend fun verifyToken(verifyToken: String): AuthResponse

    @GET("/v1/auth/reset_password")
    suspend fun resetPassword(email: String): ApiResponse

    @POST("/v1/auth/change_password")
    suspend fun changePassword(email: String, oldPassword: String, newPassword: String, passwordToken: String): ApiResponse

}