package student.testing.system.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import student.testing.system.data.dataSource.RemoteDataSource
import student.testing.system.data.dataSource.RemoteDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataSourceModule {

    @Binds
    abstract fun bindRemoteDataSource(dataSourceImpl: RemoteDataSourceImpl): RemoteDataSource
}