package app.storytel.candidate.com.di

import android.app.Application
import app.storytel.candidate.com.local.AppDatabase
import app.storytel.candidate.com.network.ApiService
import app.storytel.candidate.com.util.Constants.BASE_URL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * We use the @module annotation in order to inform hilt how to provide the instance
 * And we use InstallIn annotation in order to tell Hilt which Android class the module will be
 * used/installed in. In this case we are using the SingletonComponent class
 *  which tells it that this module will be available for complete application lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * We create a provider that will make it easier to access
     */
    @Provides
    fun provideBaseUrl() = BASE_URL

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideDao(db: AppDatabase) = db.localDao()

    @Singleton
    @Provides
    fun provideDataBase(app: Application): AppDatabase = AppDatabase.getDatabase(app)

}