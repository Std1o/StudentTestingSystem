package student.testing.system.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import student.testing.system.common.Constants
import student.testing.system.data.MainService
import student.testing.system.data.OAuthInterceptor
import student.testing.system.data.api.AuthApi
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun getHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    fun getHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient.Builder =
        OkHttpClient.Builder()
            .callTimeout(1, TimeUnit.MINUTES)
            .retryOnConnectionFailure(true)
            .addInterceptor(OAuthInterceptor())
            .addInterceptor(httpLoggingInterceptor)

    @Provides
    @Singleton
    fun provideRetrofit(httpBuilder: OkHttpClient.Builder): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .client(httpBuilder.build())
            .build()

    @Provides
    @Singleton
    fun provideMainService(retrofit: Retrofit): MainService =
        retrofit.create(MainService::class.java)

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)
}