package jp.matsuura.facediary.model

data class AuthResponse(
    val status: String,
    val errorCode: String,
    val accessToken: String,
)
