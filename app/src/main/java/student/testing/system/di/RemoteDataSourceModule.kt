package student.testing.system.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import student.testing.system.data.source.impl.AuthRemoteDataSourceImpl
import student.testing.system.data.source.impl.CourseManagementRemoteDataSourceImpl
import student.testing.system.data.source.impl.CoursesRemoteDataSourceImpl
import student.testing.system.data.source.impl.TestResultsRemoteDataSourceImpl
import student.testing.system.data.source.impl.TestsRemoteDataSourceImpl
import student.testing.system.data.source.interfaces.AuthRemoteDataSource
import student.testing.system.data.source.interfaces.CourseManagementRemoteDataSource
import student.testing.system.data.source.interfaces.CoursesRemoteDataSource
import student.testing.system.data.source.interfaces.TestResultsRemoteDataSource
import student.testing.system.data.source.interfaces.TestsRemoteDataSource

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataSourceModule {

    @Binds
    abstract fun bindAuthRemoteDataSource(dataSourceImpl: AuthRemoteDataSourceImpl): AuthRemoteDataSource

    @Binds
    abstract fun bindCoursesRemoteDataSource(dataSourceImpl: CoursesRemoteDataSourceImpl): CoursesRemoteDataSource

    @Binds
    abstract fun bindTestsRemoteDataSource(dataSourceImpl: TestsRemoteDataSourceImpl): TestsRemoteDataSource

    @Binds
    abstract fun bindCourseManagementRemoteDataSource(dataSourceImpl: CourseManagementRemoteDataSourceImpl): CourseManagementRemoteDataSource

    @Binds
    abstract fun bindTestResultsRemoteDataSource(dataSourceImpl: TestResultsRemoteDataSourceImpl): TestResultsRemoteDataSource
}