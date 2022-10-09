package jp.matsuura.facediary.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class NormalRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class RetrofitForFaceApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class NormalOkHttp

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class OkHttpForFaceApi