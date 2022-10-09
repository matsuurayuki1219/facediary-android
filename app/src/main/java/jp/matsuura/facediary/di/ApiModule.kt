package jp.matsuura.facediary.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.matsuura.facediary.data.api.AuthApi
import jp.matsuura.facediary.data.api.CalendarApi
import jp.matsuura.facediary.data.api.FaceApi
import retrofit2.Retrofit
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    fun provideAuthApi(@NormalRetrofit retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    fun provideCalendarApi(@NormalRetrofit retrofit: Retrofit): CalendarApi {
        return retrofit.create(CalendarApi::class.java)
    }

    @Provides
    fun provideFaceApi(@RetrofitForFaceApi retrofit: Retrofit): FaceApi {
        return retrofit.create(FaceApi::class.java)
    }

}