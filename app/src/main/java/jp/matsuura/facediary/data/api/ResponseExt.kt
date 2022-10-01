package jp.matsuura.facediary.data.api

import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import jp.matsuura.facediary.data.api.entity.ErrorEntity
import retrofit2.HttpException
import retrofit2.Response

inline fun <Entity, reified Model> Response<Entity>.toModel(crossinline converter: (Entity) -> Model): Model {
    val body = body()
    if (isSuccessful && body != null) {
        return converter(body)
    } else {
        throw HttpException(this)
    }
}

@Throws(JsonDataException::class)
inline fun <reified T> Response<*>.getErrorResponse(): T? {
    val jsonStr = errorBody()?.string()
    val adapter = Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter(T::class.java)
    return if (jsonStr != null) {
        adapter.fromJson(jsonStr)
    } else {
        null
    }
}