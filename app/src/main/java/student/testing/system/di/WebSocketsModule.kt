package student.testing.system.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import student.testing.system.data.api.AuthApi
import student.testing.system.data.api.KtorWebsocketClientImpl
import student.testing.system.data.repository.AuthRepositoryImpl
import student.testing.system.data.repository.CoursesRepositoryImpl
import student.testing.system.data.repository.CourseManagementRepositoryImpl
import student.testing.system.data.repository.TestsRepositoryImpl
import student.testing.system.domain.repository.AuthRepository
import student.testing.system.domain.repository.CoursesRepository
import student.testing.system.domain.repository.CourseManagementRepository
import student.testing.system.domain.repository.TestsRepository
import student.testing.system.domain.webSockets.KtorWebsocketClient
import student.testing.system.domain.webSockets.WebsocketEvents
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

class WebSocketsModule {

    @Provides
    @Singleton
    fun provideKtorWebsocketClient(): KtorWebsocketClient {
        val client = KtorWebsocketClientImpl(url = "wss://testingsystem.ru/tests/ws/results/58?course_id=1",
                object : WebsocketEvents {
                    override fun onReceive(data: String) {
                        println("onReceive")
                    }

                    override fun onConnected() {
                        println("onConnected")
                    }

                    override fun onDisconnected(reason: String) {
                        println("onDisconnected")
                    }
                })
        return client
    }
}