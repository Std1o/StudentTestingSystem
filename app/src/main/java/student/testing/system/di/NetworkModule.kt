package student.testing.system.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import student.testing.system.common.Constants
import student.testing.system.data.OAuthInterceptor
import student.testing.system.data.api.AuthApi
import student.testing.system.data.api.CourseManagementApi
import student.testing.system.data.api.CoursesApi
import student.testing.system.data.api.TestsApi
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun getHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    fun getHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        @ApplicationContext context: Context
    ): OkHttpClient.Builder =
        OkHttpClient.Builder()
            .callTimeout(1, TimeUnit.MINUTES)
            .retryOnConnectionFailure(true)
            .addInterceptor(OAuthInterceptor())
            .addInterceptor(httpLoggingInterceptor)
            .addNetworkInterceptor(ChuckerInterceptor(context))

    @Provides
    @Singleton
    fun provideRetrofit(httpBuilder: OkHttpClient.Builder): Retrofit {
        val builder = GsonBuilder().disableHtmlEscaping().create()
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(builder))
            .baseUrl(Constants.BASE_URL)
            .client(httpBuilder.build())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideCoursesApi(retrofit: Retrofit): CoursesApi = retrofit.create(CoursesApi::class.java)

    @Provides
    @Singleton
    fun provideTestsApi(retrofit: Retrofit): TestsApi = retrofit.create(TestsApi::class.java)

    @Provides
    @Singleton
    fun provideCourseManagementApi(retrofit: Retrofit): CourseManagementApi =
        retrofit.create(CourseManagementApi::class.java)
}