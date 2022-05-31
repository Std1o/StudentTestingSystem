package student.testing.system.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import student.testing.system.api.network.MainRemoteData
import student.testing.system.api.network.MainService
import student.testing.system.api.network.OAuthInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesBaseUrl() : String = "http://ezapitest.ml/"

    @Provides
    fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    fun getHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .callTimeout(1, TimeUnit.MINUTES)
            .retryOnConnectionFailure(true)
            .addInterceptor(OAuthInterceptor())
            .addInterceptor(httpLoggingInterceptor)
    }

    @Provides
    @Singleton
    fun provideRetrofit(BASE_URL : String, httpBuilder: OkHttpClient.Builder) : Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(httpBuilder.build())
        .build()

    @Provides
    @Singleton
    fun provideMainService(retrofit : Retrofit) : MainService = retrofit.create(MainService::class.java)

    @Provides
    @Singleton
    fun provideMainRemoteData(mainService : MainService) : MainRemoteData = MainRemoteData(mainService)
}