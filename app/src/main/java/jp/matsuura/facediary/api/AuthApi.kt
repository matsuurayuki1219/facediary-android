package jp.matsuura.facediary.api

import jp.matsuura.facediary.model.AuthResponse
import retrofit2.http.GET

interface AuthApi {

    @GET("/v1/auth/login")
    fun login(userId: String, password: String): AuthResponse

}