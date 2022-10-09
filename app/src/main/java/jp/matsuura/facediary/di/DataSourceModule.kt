package jp.matsuura.facediary.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jp.matsuura.facediary.data.datasource.AppPreference
import jp.matsuura.facediary.data.datasource.CacheDir
import java.io.File
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    fun provideFaceDiaryPreference(@ApplicationContext context: Context): AppPreference {
        return AppPreference(context)
    }

    @Provides
    @Named("CACHE_DIR")
    fun provideCacheDir(@ApplicationContext context: Context) : File {
        return context.cacheDir
    }

}