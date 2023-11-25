package student.testing.system.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import student.testing.system.data.source.impl.AuthRemoteDataSourceImpl
import student.testing.system.data.source.interfaces.RemoteDataSource
import student.testing.system.data.source.impl.RemoteDataSourceImpl
import student.testing.system.data.source.interfaces.AuthRemoteDataSource

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataSourceModule {

    @Binds
    abstract fun bindRemoteDataSource(dataSourceImpl: RemoteDataSourceImpl): RemoteDataSource

    @Binds
    abstract fun bindAuthRemoteDataSource(dataSourceImpl: AuthRemoteDataSourceImpl): AuthRemoteDataSource
}