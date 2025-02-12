package student.testing.system.common

import javax.inject.Qualifier

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class LaunchNavigation

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class CourseReviewNavigation

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class TestCreationNavigation
