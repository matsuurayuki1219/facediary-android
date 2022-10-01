package jp.matsuura.facediary.common

sealed class Response <out T, out E> {
    data class Success<out V>(val value: V): Response<V, Nothing>()
    data class Error<out E>(val error: E): Response<Nothing, E>()
}