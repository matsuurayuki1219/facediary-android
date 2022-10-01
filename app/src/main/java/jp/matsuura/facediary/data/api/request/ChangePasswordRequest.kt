package jp.matsuura.facediary.data.api.request

data class ChangePasswordRequest (
    val email: String,
    val password: String,
    val token: String,
)