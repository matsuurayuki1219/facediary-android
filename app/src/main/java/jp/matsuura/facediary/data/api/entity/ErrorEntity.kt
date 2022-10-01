package jp.matsuura.facediary.data.api.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorEntity(
    val message: String,
    val errorCode: String,
)