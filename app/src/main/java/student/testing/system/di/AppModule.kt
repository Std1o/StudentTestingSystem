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
import student.testing.system.common.Constants.COURSE_REVIEW_NAVIGATION
import student.testing.system.common.Constants.LAUNCH_NAVIGATION
import student.testing.system.common.Constants.SHARED_PREFERENCES_NAME
import student.testing.system.common.Constants.TEST_CREATION_NAVIGATION
import student.testing.system.data.repository.MainRepositoryImpl
import student.testing.system.domain.MainRepository
import student.testing.system.data.dataSource.RemoteDataSource
import student.testing.system.presentation.navigation.AppNavigator
import student.testing.system.presentation.navigation.AppNavigatorImpl
import student.testing.system.sharedPreferences.PrefsUtils
import student.testing.system.sharedPreferences.PrefsUtilsImpl
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

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