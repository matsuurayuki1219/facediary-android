package jp.matsuura.facediary.data.api.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiEntity(
    val message: String,
)