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
import student.testing.system.common.Constants.SHARED_PREFERENCES_NAME
import student.testing.system.common.CourseReviewNavigation
import student.testing.system.common.LaunchNavigation
import student.testing.system.common.TestCreationNavigation
import student.testing.system.presentation.navigation.AppNavigator
import student.testing.system.presentation.navigation.AppNavigatorImpl
import student.testing.system.sharedPreferences.PrefsUtils
import student.testing.system.sharedPreferences.PrefsUtilsImpl
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

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
    @LaunchNavigation
    fun provideLaunchNavigation() = AppNavigatorImpl() as AppNavigator

    @Singleton
    @Provides
    @CourseReviewNavigation
    fun provideCourseReviewNavigation() = AppNavigatorImpl() as AppNavigator

    @Singleton
    @Provides
    @TestCreationNavigation
    fun provideTestCreationNavigation() = AppNavigatorImpl() as AppNavigator
}