package jp.matsuura.facediary.api.request

data class LoginRequest (
    val email: String,
    val password: String,
)