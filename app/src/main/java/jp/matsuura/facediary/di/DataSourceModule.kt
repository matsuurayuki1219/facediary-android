package jp.matsuura.facediary.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jp.matsuura.facediary.data.datasource.FaceDiaryPreference

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    fun provideFaceDiaryPreference(@ApplicationContext context: Context): FaceDiaryPreference {
        return FaceDiaryPreference(context)
    }
}