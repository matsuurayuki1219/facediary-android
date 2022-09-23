package jp.matsuura.facediary.api.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiResponse(
    val message: String,
)