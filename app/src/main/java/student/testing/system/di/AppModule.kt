package student.testing.system.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import student.testing.system.common.Constants.BASE_URL
import student.testing.system.common.Constants.COURSE_REVIEW_NAVIGATION
import student.testing.system.common.Constants.LAUNCH_NAVIGATION
import student.testing.system.data.dataSource.RemoteDataSourceImpl
import student.testing.system.data.MainService
import student.testing.system.data.OAuthInterceptor
import student.testing.system.common.Constants.SHARED_PREFERENCES_NAME
import student.testing.system.common.Constants.TEST_CREATION_NAVIGATION
import student.testing.system.data.repository.MainRepositoryImpl
import student.testing.system.domain.MainRepository
import student.testing.system.data.dataSource.RemoteDataSource
import student.testing.system.presentation.navigation.AppNavigator
import student.testing.system.presentation.navigation.AppNavigatorImpl
import student.testing.system.sharedPreferences.PrefsUtils
import student.testing.system.sharedPreferences.PrefsUtilsImpl
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

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
            .baseUrl(BASE_URL)
            .client(httpBuilder.build())
            .build()

    @Provides
    @Singleton
    fun provideMainService(retrofit: Retrofit): MainService =
        retrofit.create(MainService::class.java)

    @Provides
    @Singleton
    fun provideRemoteDataSource(mainService: MainService) =
        RemoteDataSourceImpl(mainService) as RemoteDataSource

    @Singleton
    @Provides
    fun provideRepository(remoteDataSource: RemoteDataSource) =
        MainRepositoryImpl(remoteDataSource) as MainRepository

    @Provides
    @Singleton
    fun provideEncryptedSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        return EncryptedSharedPreferences.create(
            context,
            SHARED_PREFERENCES_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    @Singleton
    @Provides
    fun providePrefsUtils(
        sharedPreferences: SharedPreferences
    ) = PrefsUtilsImpl(sharedPreferences) as PrefsUtils

    @Singleton
    @Provides
    @Named(LAUNCH_NAVIGATION)
    fun provideLaunchNavigation() = AppNavigatorImpl() as AppNavigator

    @Singleton
    @Provides
    @Named(COURSE_REVIEW_NAVIGATION)
    fun provideCourseReviewNavigation() = AppNavigatorImpl() as AppNavigator

    @Singleton
    @Provides
    @Named(TEST_CREATION_NAVIGATION)
    fun provideTestCreationNavigation() = AppNavigatorImpl() as AppNavigator
}