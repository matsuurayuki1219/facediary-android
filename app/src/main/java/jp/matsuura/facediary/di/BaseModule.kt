package jp.matsuura.facediary.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.matsuura.facediary.BuildConfig
import jp.matsuura.facediary.common.Constant
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object BaseModule {

    private val MOSHI: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @NormalRetrofit
    fun provideNormalRetrofit(@NormalOkHttp httpClient: OkHttpClient.Builder): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(MOSHI))
            .client(httpClient.build())
            .build()
    }

    @Provides
    @RetrofitForFaceApi
    fun provideRetrofitForFaceApi(@OkHttpForFaceApi httpClient: OkHttpClient.Builder): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://facemusicapp.cognitiveservices.azure.com")
            .addConverterFactory(MoshiConverterFactory.create(MOSHI))
            .client(httpClient.build())
            .build()
    }

    @Provides
    @NormalOkHttp
    fun provideNormalOkHttpClient(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .connectTimeout(30L, TimeUnit.SECONDS)
            .readTimeout(30L, TimeUnit.SECONDS)
            .writeTimeout(30L, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                chain.request().let {
                    val builder = it.newBuilder()
                    builder.addHeader("Content-Type", "application/json")
                    chain.proceed(builder.method(it.method, it.body).build())
                }
            }
    }

    @Provides
    @OkHttpForFaceApi
    fun provideOkHttpForFaceApiClient(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .connectTimeout(30L, TimeUnit.SECONDS)
            .readTimeout(30L, TimeUnit.SECONDS)
            .writeTimeout(30L, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                chain.request().let {
                    val builder = it.newBuilder()
                    builder.addHeader("Content-Type", "application/octet-stream")
                    builder.addHeader("Ocp-Apim-Subscription-Key", BuildConfig.FACEAPI_SUBSCRIPTION_KEY)
                    chain.proceed(builder.method(it.method, it.body).build())
                }
            }
    }

}