package jp.matsuura.facediary.data.model

import jp.matsuura.facediary.data.api.entity.AuthEntity

class AuthModel (
    val accessToken: String,
)

fun AuthEntity.toModel(): AuthModel {
    return AuthModel(
        accessToken = accessToken
    )
}