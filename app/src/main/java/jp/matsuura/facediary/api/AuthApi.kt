package jp.matsuura.facediary.api

import jp.matsuura.facediary.model.AuthResponse
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {

    @GET("/v1/auth/login")
    fun login(userId: String, password: String): AuthResponse

    @POST("/v1/auth/createUser")
    fun createUser(userId: String, password: String): AuthResponse

    @GET("/v1/auth/resetPassword")
    fun resetPassword(userId: String): AuthResponse

}